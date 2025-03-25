package com.example.baozi_list.util

import android.content.Context
import android.content.res.Resources
import android.view.View

fun dip2px(context: Context, dpValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun isTouchInsideView(view: View, x: Float, y: Float): Boolean {
    val location = IntArray(2)
    view.getLocationOnScreen(location)
    val viewX = location[0]
    val viewY = location[1]
    val viewWidth = view.width
    val viewHeight = view.height
    return (x >= viewX && x <= (viewX + viewWidth)) && (y >= viewY && y <= (viewY + viewHeight))
}


/**
 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 */
fun dp2px(resources: Resources, dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
 */
fun px2dp(resources: Resources,pxValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}