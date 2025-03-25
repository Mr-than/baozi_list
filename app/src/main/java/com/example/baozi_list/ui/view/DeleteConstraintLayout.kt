package com.example.baozi_list.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.Scroller
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.example.baozi_list.util.isTouchInsideView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs


class DeleteConstraintLayout : ConstraintLayout {

    private val touchSlop by lazy {
        val configuration = ViewConfiguration.get(context)
        configuration.scaledPagingTouchSlop
    }
    private var downX = 0f
    private var downY = 0f
    private var lastX = 0f
    private var isRecover = true
    private var allowBackDistance = 0
    private var accumulateX = 0

    private val scroller = Scroller(context)
    private val deleteButton by lazy {
        var view: View? = null
        for (v in children) {
            if (v.tag == "delete_button") {
                view = v
            }
        }
        assert(view != null)
        view!!
    }
    private val delegateButtonText by lazy {
        var view: View? = null
        (deleteButton as? ViewGroup)?.children?.let {
            for (v in it) {
                if (v.tag == "delete_button_text") {
                    view = v
                }
            }
        }
        assert(view != null)
        view!!
    }
    private val deleteButtonWidth by lazy { deleteButton.width }
    private val deleteButtonTextWidth by lazy { delegateButtonText.width }
    private val minOpenDistance by lazy { deleteButtonTextWidth / 2 }

    private var isMove = false


    private val job = Job()
    private val scope = CoroutineScope(job)
    private var isLongPress = false


    companion object{
        @Volatile
        private var tempJob: Job? = null // 定义为可空类型
    }

    fun setRecycleViewListener(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    cancelJob()
                }
            }
        })
    }

    fun setDownViewListener(scrollerDownView: ScrollerDownView){
        scrollerDownView.addOnScrollListener(object:ScrollerDownView.OnScrollListener{
            override fun onScroll(dy: Int) {
                cancelJob()
            }
        })
    }


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.rawX
                downY = event.rawY
                lastX = downX
                isMove = false

                isLongPress = false
                handleLongPress(scope)
            }
            MotionEvent.ACTION_MOVE -> {
                if (abs((event.rawX - downX)) > touchSlop || abs(event.rawY - downY) > touchSlop) {
                    cancelJob()
                }
                if (event.rawX - lastX < 0) {
                    val margin = deleteButtonWidth - deleteButtonTextWidth
                    if (abs(event.rawX - downX) > touchSlop) {
                        isMove = true
                        requestDisallowInterceptTouchEvent(true)

                        val d = if (abs(accumulateX) > deleteButtonTextWidth) {
                            val moveMargin = (abs(accumulateX) - deleteButtonTextWidth).toFloat()
                            val v = event.rawX - lastX
                            val m = if ((margin - moveMargin) / margin > 0) {
                                ((margin - moveMargin) / margin)
                            } else {
                                0f
                            }
                            -(m * v).toInt()
                        } else {
                            -(event.rawX - lastX).toInt()
                        }
                        if (abs(accumulateX) > deleteButtonTextWidth) {
                            allowBackDistance = abs(accumulateX) - abs(deleteButtonTextWidth)
                        }
                        accumulateX += d
                        scrollBy(d, 0)
                        isRecover = false
                    }
                } else if (event.rawX - lastX > 0 && !isRecover) {
                    if (abs(event.rawX - downX) > touchSlop) {
                        isMove = true
                        requestDisallowInterceptTouchEvent(true)
                    }
                    scrollBy(-(event.rawX - lastX).toInt(), 0)
                    accumulateX += -(event.rawX - lastX).toInt()
                    if (accumulateX <= 0) {
                        accumulateX = 0
                        allowBackDistance = 0
                        isRecover = true
                    }
                }
                lastX = event.rawX
            }

            MotionEvent.ACTION_UP -> {
                if (isMove) {
                    if (abs(accumulateX) > deleteButtonTextWidth) {
                        scroller.startScroll(accumulateX, 0, -allowBackDistance, 0)
                        allowBackDistance = deleteButtonTextWidth
                        accumulateX = deleteButtonTextWidth
                        invalidate()
                    } else if (abs(accumulateX) < minOpenDistance) {
                        scroller.startScroll(accumulateX, 0, -accumulateX, 0)
                        allowBackDistance = 0
                        accumulateX = 0
                        isRecover = true
                        invalidate()
                    } else if (abs(accumulateX) in (minOpenDistance + 1)..<deleteButtonTextWidth) {
                        scroller.startScroll(accumulateX, 0, deleteButtonTextWidth - accumulateX, 0)
                        allowBackDistance = deleteButtonTextWidth
                        accumulateX = deleteButtonTextWidth
                        invalidate()
                    }
                } else {
                    if (!isRecover && isTouchInsideView(deleteButton, event.rawX, event.rawY)) {
                        deleteButton.performClick()
                        return false
                    } else if (!isRecover && !isTouchInsideView(
                            deleteButton,
                            event.rawX,
                            event.rawY
                        )
                    ) {
                        // TODO: 添加点击回调
                        recover()
                    } else if (isRecover && !isLongPress) {
                        performClick()
                    }
                }
                cancelJob()
            }

            MotionEvent.ACTION_CANCEL -> {
                cancelJob()
                recover()
            }
        }
        return true
    }


    fun recover() {
       if(!isRecover){
           scroller.startScroll(accumulateX, 0, -accumulateX, 0)
           allowBackDistance = 0
           accumulateX = 0
           isRecover = true
           invalidate()
       }
    }

    private fun handleLongPress(scope: CoroutineScope) {
        // 检查是否存在正在运行的 Job，如果存在则取消
        tempJob?.let {
            if (it.isActive) {
                it.cancel()
            }
            tempJob = null // 显式将 tempJob 置为 null
        }
        // 创建新的协程 Job
        tempJob = scope.launch(Dispatchers.Main) {
            delay(500) // 挂起，等待 500 毫秒
            tempJob?.let {
                if (isActive) { // 检查当前协程是否仍然是活动状态
                    isLongPress = true
                    if(isAttachedToWindow)
                        performLongClick()
                }
            }
        }
    }

    private fun cancelJob() {
        tempJob?.let {
            if (it.isActive) {
                it.cancel()
            }
            tempJob = null // 显式将 tempJob 置为 null
        }
    }


    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            invalidate()
        }
    }
}