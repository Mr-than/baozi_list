package com.example.baozi_list.ui.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.baozi_list.util.dip2px
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

class MoveFloatingActionButton : FloatingActionButton {


    private val displayMetrics = resources.displayMetrics
    private val screenWidth by lazy { displayMetrics.widthPixels }
    private val screenHeight by lazy { displayMetrics.heightPixels }
    private val limitNumX = dip2px(context, 16f)
    private val limitNumY = dip2px(context, 150f)
    private val touchSlop by lazy {
        val configuration = ViewConfiguration.get(context)
        configuration.scaledPagingTouchSlop
    }

    private var dowX: Int = 0
    private var dowY: Int = 0
    private var lastX: Int = 0
    private var lastY: Int = 0


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val job = Job()
    private val scope = CoroutineScope(job)
    private var isLongPress = false
    private lateinit var tempJob: Job
    private lateinit var setter: MainActivityFloatButtonAnimatorSetter


    fun setAnimatorSetter(setter: MainActivityFloatButtonAnimatorSetter){
        this.setter = setter
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dowX = event.rawX.toInt()
                dowY = event.rawY.toInt()
                lastX = dowX
                lastY = dowY
                isLongPress = false
                tempJob = scope.launch(Dispatchers.Main) {
                    delay(500)
                    isLongPress = true
                    performLongClick()
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (abs((event.rawX - dowX)) > touchSlop || abs(event.rawY - dowY) > touchSlop) {
                    tempJob.cancel()
                    setter.closeButton()
                }
                val dx = event.rawX.toInt() - lastX //位移量X
                val dy = event.rawY.toInt() - lastY //位移量Y

                val high = height

                var left = left + dx
                var top = top + dy
                var right = right + dx
                var bottom = bottom + dy

                if (top < limitNumY) {
                    top = limitNumY
                    bottom = high + limitNumY
                } else if (bottom > screenHeight - limitNumY) {
                    top = screenHeight - limitNumY - high
                    bottom = screenHeight - limitNumY
                }

                if (left < limitNumX) {
                    left = limitNumX
                    right = width + limitNumX
                } else if (right > screenWidth - limitNumX) {
                    left = screenWidth - width - limitNumX
                    right = screenWidth - limitNumX
                }

                layout(left, top, right, bottom)
                lastX = event.rawX.toInt()
                lastY = event.rawY.toInt()
            }

            MotionEvent.ACTION_UP -> {
                if (abs((event.rawX - dowX)) > 5 || abs((event.rawY - dowY)) > 5) {

                    (parent as? ConstraintLayout)?.animation?.cancel()

                    val left: Int = left
                    val marginRight = screenWidth - right
                    val animator = ValueAnimator()

                    animator.setTarget(parent)
                    animator.setDuration(100)

                    if (left < marginRight) {
                        animator.setIntValues(left, limitNumX)
                    } else {
                        animator.setIntValues(
                            left,
                            screenWidth - width - limitNumX
                        )
                    }

                    animator.addUpdateListener { animation ->
                        val value = animation.getAnimatedValue() as Int
                        layout(value, top, value + width, bottom)
                    }
                    animator.start()
                } else {
                    if (isClickable && !isLongPress) {
                        performClick()
                    }
                }
                tempJob.cancel()
            }

            else -> {
                return false
            }
        }
        return true
    }
}
