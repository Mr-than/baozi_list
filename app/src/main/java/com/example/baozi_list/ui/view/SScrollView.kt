package com.example.baozi_list.ui.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.ScrollView
import com.example.baozi_list.ui.MainActivity

class SScrollView:ScrollView {
    private var activity: MainActivity? = null
    constructor(context: Context):super(context){
    }
    constructor(context: Context, attrs: AttributeSet):super(context,attrs){
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int):super(context,attrs,defStyleAttr){
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
            if(ev?.action == MotionEvent.ACTION_DOWN){
                if (isSoftShowing()) {
                    val imm=activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(windowToken, 0)
                }
            }
        return super.onInterceptTouchEvent(ev)
    }

    private fun isSoftShowing(): Boolean {
        //获取当前屏幕内容的高度
        val screenHeight: Int =( activity?.window?.decorView?.height)?:0
        //获取View可见区域的bottom
        val rect = Rect()
        activity?.window?.decorView?.getWindowVisibleDisplayFrame(rect)

        return screenHeight - rect.bottom != 0
    }

    fun setActivity(activity: MainActivity){
        this.activity = activity

    }

}