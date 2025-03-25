package com.example.baozi_list.ui.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.example.baozi_list.databinding.CenterActivityMainBinding
import com.example.baozi_list.util.isTouchInsideView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.log2

class ScrollerDownView : ViewGroup {

    //下拉的动画等时间变量
    companion object {
        const val DOWN_TIME = 500
        const val UP_TIME = 500
    }


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    private lateinit var centerBinding: CenterActivityMainBinding
    private var totalY = 0
    private var totalDownY = 0
    private var refreshY: Int = 0
    private var firstTop: Int = 0


    //滑动有关变量
    private val mTracker = VelocityTracker.obtain()
    private var downY: Int = 0
    private var lastY: Int = 0


    private var mulY = 0

    private var downX = 0
    private var lastX = 0

    private var totalBackY = 0
    private var height: Int = 0
    private var isBackUp = false
    private var isClickButton = false
    private var isTop = false
    val isDown: Boolean
        get() = isTop

    private var sideScroll = false
    private var downScroll = false


    private val scrollerUp = Scroller(context)
    private val backTestPage = Scroller(context)

    private val touchSlop by lazy {
        val configuration = ViewConfiguration.get(context)
        configuration.scaledPagingTouchSlop
    }


    // 创建一个恢复到原大小的动画
    private val recoverAnimator = ValueAnimator.ofFloat(0.6f, 1.0f).apply {
        interpolator = DecelerateInterpolator()
        addUpdateListener { animation ->
            val scale = animation.animatedValue as Float
            topView.scaleX = scale
            topView.scaleY = scale
        }
    }

    private val shrinkAnimator = ValueAnimator.ofFloat(1f, 0.6f).apply {
        interpolator = DecelerateInterpolator()
        addUpdateListener { animation ->
            val scale = animation.animatedValue as Float
            topView.scaleX = scale
            topView.scaleY = scale
        }
    }


    private lateinit var topView: View

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val thisMeasureWith = MeasureSpec.getSize(widthMeasureSpec)
        val thisMeasureHeight = MeasureSpec.getSize(heightMeasureSpec)
        val measureWithMode = MeasureSpec.getMode(widthMeasureSpec)
        val measureHeightMode = MeasureSpec.getMode(heightMeasureSpec)

        var height = 0
        var width = 0
        val count = childCount

        for (i in 0 until count) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            width = child.measuredWidth
            height += child.measuredHeight
        }

        setMeasuredDimension(
            if (measureWithMode == MeasureSpec.EXACTLY) {
                thisMeasureWith
            } else {
                width
            },
            if (measureHeightMode == MeasureSpec.EXACTLY) {
                thisMeasureHeight
            } else {
                height
            }
        )

    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {

        var height = 0
        val count = childCount

        for (i in 0 until count) {
            val child = getChildAt(i)
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight

            child.layout(0, height, childWidth, height + childHeight)
            if (i + 1 < count) {
                height -= getChildAt(i + 1).measuredHeight
            }
        }

        firstTop = getChildAt(0).top
        refreshY = getChildAt(count - 1).bottom
        centerBinding = CenterActivityMainBinding.bind(getChildAt(1))

        this.height = getChildAt(0).height

    }

    fun setTopView(view: View) {
        view.scaleX = 0.6f
        view.scaleY = 0.6f
        topView = view
    }


    private fun <T : View> getViewGroupRecyclerView(
        viewGroup: ViewGroup,
        amount: Int,
        clazz: Class<T>
    ): T? {
        if (amount > 4) {
            return null
        }
        for (j in 0 until viewGroup.childCount) {
            val c1 = viewGroup.getChildAt(j)
            if (clazz.isAssignableFrom(c1.javaClass)) {
                return c1 as T
            } else {
                if (c1 is ViewGroup) {
                    val view = getViewGroupRecyclerView(c1, amount + 1, clazz) ?: continue
                    if (clazz.isAssignableFrom(view::class.java)) {
                        return view
                    }
                }
            }
        }
        return null
    }

    private val recyclerView by lazy {
        val child = getChildAt(0)
        val view = getViewGroupRecyclerView(child as ViewGroup, 0, RecyclerView::class.java)
        view ?: throw Exception("没有找到RecyclerView")
    }

    private val floatButton by lazy {
        val child = getChildAt(0)
        val view =
            getViewGroupRecyclerView(child as ViewGroup, 0, MoveFloatingActionButton::class.java)
        view ?: throw Exception("没有找到MoveFloatingActionButton")
    }

    private fun getV(v: Int): Double {
        return 1 / log2(3.0) * log2(v.toDouble())
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        //这里要判断是否点击了悬浮按钮，默认不拦截，不判断按钮会出问题
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downY = ev.rawY.toInt()
                lastY = downY

                downX = ev.rawX.toInt()
                lastX = downX

                val left = floatButton.left
                val top = floatButton.top
                val right = floatButton.right
                val bottom = floatButton.bottom
                val x = ev.x
                val y = ev.y
                if (x > left && x < right && y > top && y < bottom) {
                    isClickButton = true
                    return false
                }
            }

            MotionEvent.ACTION_MOVE -> {

                if (isClickButton) {
                    return false
                }

                lastX = ev.rawX.toInt()
                lastY = ev.rawY.toInt()
            }

            MotionEvent.ACTION_UP -> {
                return false
            }
        }
        //默认拦截，交给onTouchEvent处理，这里处理不是很方便，并且会导致切换事件控制权时卡顿
        return true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mTracker.addMovement(event)
        mTracker.computeCurrentVelocity(50)
        val yVelocity = mTracker.yVelocity
        val xVelocity = mTracker.xVelocity

        if (isTop) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (isTouchInsideView(centerBinding.root, event.rawX, event.rawY)) {
                        downY = event.rawY.toInt()
                        lastY = downY
                        return true
                    }
                    return false
                }

                MotionEvent.ACTION_MOVE -> {
                    if (abs(event.rawY.toInt() - downY) > touchSlop) {
                        val d = -(event.rawY.toInt() - lastY)
                        scrollBy(0, d)
                        totalDownY += d
                    }
                    lastY = event.rawY.toInt()
                }

                MotionEvent.ACTION_UP -> {
                    Log.d("===============", "onTouchEvent: $yVelocity")

                    if (abs(event.rawY.toInt() - downY) > touchSlop) {


                        if (yVelocity < 0) {
                            backTestPage.startScroll(
                                0,
                                totalDownY - height,
                                0,
                                -(totalDownY - height),
                                DOWN_TIME
                            )
                            CoroutineScope(Job()).launch {
                                delay(DOWN_TIME.toLong())
                                topView.scaleX = 0.6f
                                topView.scaleY = 0.6f
                            }
                        } else {
                            scrollerUp.startScroll(0, -totalDownY + height, 0, totalDownY, UP_TIME)
                        }
                        totalDownY = 0
                        invalidate()


                    } else {
                        backDown()
                    }
                }
            }
            return false
        }


        //recycleView点击事件和滑动等处理
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                children.first().dispatchTouchEvent(event)
                downY = event.rawY.toInt()
                lastY = downY
                downX = event.rawX.toInt()
                lastX = downX
            }

            MotionEvent.ACTION_MOVE -> {
                if (!downScroll && abs(event.rawX.toInt() - downX) > touchSlop && abs(event.rawY.toInt() - downY) < touchSlop && abs(
                        xVelocity
                    ) >= 30 && 0 < (yVelocity) && (yVelocity) < 50
                ) {
                    sideScroll = true
                    children.first().dispatchTouchEvent(event)
                } else {

                    isBackUp = true
                    val dy = event.rawY.toInt() - lastY

                    if (((!recyclerView.canScrollVertically(-1) && dy > 0)) && abs(event.rawY.toInt() - downY) > touchSlop && !sideScroll) {
                        downScroll = true
                        if (::scrollListenerBlock.isInitialized)
                            scrollListenerBlock.onScroll(1)
                        if (yVelocity > 0) {
                            if (dy - totalY > 2) {
                                val v = (if ((yVelocity) < 2000) yVelocity else 2000).toInt()
                                var d = (-getV(dy) * (v / 60.0)).toInt()
                                d = if (abs(d) < 200) d else -200
                                totalDownY += d
                                scrollBy(0, d)
                            }
                        } else {
                            val d = totalY - dy
                            totalBackY += d
                            if (abs(totalBackY) >= abs(totalDownY)) {
                                scrollTo(0, 0)
                                totalBackY = 0
                                totalDownY = 0
                            } else {
                                if (isBackUp && abs(d) < 200) {
                                    scrollBy(0, d)
                                }
                            }
                        }
                    } else {
                        children.first().dispatchTouchEvent(event)
                    }
                    totalY = dy
                }
                lastX = event.rawX.toInt()
                mulY = event.rawY.toInt()
            }

            MotionEvent.ACTION_UP -> {
                sideScroll = false
                downScroll = false
                if (::scrollListenerBlock.isInitialized)
                    scrollListenerBlock.onScroll(2)
                if (totalDownY == 0)
                    children.first().dispatchTouchEvent(event)
                if (abs(totalDownY) > 300) {
                    val d = -getChildAt(0).height
                    scrollerUp.startScroll(0, abs(totalDownY), 0, abs(d) - abs(totalDownY), UP_TIME)
                    invalidate()
                    if (::topView.isInitialized) {
                        recoverAnimator.duration = UP_TIME.toLong()
                        recoverAnimator.start()
                    }
                } else {
                    isBackUp = false
                    //这里是处理滑动的，如果滑动的距离不够，就回到原来的位置
                    backTestPage.startScroll(0, -abs(totalDownY), 0, abs(totalDownY), DOWN_TIME)
                    invalidate()
                }
                totalDownY = 0
            }
        }
        return true
    }

    fun backDown() {
        if (::topView.isInitialized) {
            shrinkAnimator.duration = DOWN_TIME.toLong()
            shrinkAnimator.start()
        }
        backTestPage.startScroll(0, -height, 0, height, DOWN_TIME)
        invalidate()
    }

    fun toTop() {
        scrollerUp.startScroll(0, 0, 0, height, UP_TIME)
    }

    override fun computeScroll() {
        if (scrollerUp.computeScrollOffset()) {
            val x = scrollerUp.currX
            val y = -scrollerUp.currY
            scrollTo(x, y)
            if (::scrollListenerBlock.isInitialized)
                scrollListenerBlock.onScroll(1)
            invalidate()
            isTop = true
        }
        if (backTestPage.computeScrollOffset()) {
            val x = backTestPage.currX
            val y = backTestPage.currY
            scrollTo(x, y)
            if (::scrollListenerBlock.isInitialized)
                scrollListenerBlock.onScroll(1)
            invalidate()
            isTop = false
        }
    }


    interface OnScrollListener {
        fun onScroll(dy: Int)
    }

    private lateinit var scrollListenerBlock: OnScrollListener

    fun addOnScrollListener(block: OnScrollListener) {
        scrollListenerBlock = block
    }


}