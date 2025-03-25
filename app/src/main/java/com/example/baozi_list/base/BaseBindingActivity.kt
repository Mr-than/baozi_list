package com.example.baozi_list.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseBindingActivity<T : ViewBinding> : AppCompatActivity() {

    private val displayMetrics by lazy { getResources().displayMetrics }
    protected val screenWidth by lazy { displayMetrics.widthPixels }
    protected val screenHeight by lazy { displayMetrics.heightPixels }

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BindingHelper.getBindingView(javaClass, layoutInflater)

    }

    protected fun <T : View> Int.findView(): T {
        return findViewById(this)
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    protected fun dp2px( dpValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    protected fun px2dp(pxValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }


}