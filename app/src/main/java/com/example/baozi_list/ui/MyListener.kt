package com.example.baozi_list.ui

import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView


class MyListener :
    RecyclerView.SimpleOnItemTouchListener() {

        private var downX = 0f
        private var downY = 0f
        private var lastX = 0f
        private var lastY = 0f

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        when(e.action){
            MotionEvent.ACTION_DOWN -> {
                downX = e.x
                downY = e.y
            }

            MotionEvent.ACTION_MOVE -> {
                if(e.rawY - lastY > 20){
                    return false
                }

                lastX = e.rawX
                lastY = e.rawY
            }

            MotionEvent.ACTION_UP -> {
                lastX = e.x
                lastY = e.y
                if (downX == lastX && downY == lastY) {
                    val child = rv.findChildViewUnder(e.x, e.y)
                    if (child != null) {
                        Log.d("===============", "onInterceptTouchEvent: position: ${rv.getChildAdapterPosition(child)}")
                    }
                }
            }
        }
        return false
    }



    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        super.onTouchEvent(rv, e)
    }

}