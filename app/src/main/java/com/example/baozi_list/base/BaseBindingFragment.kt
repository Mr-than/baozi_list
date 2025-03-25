package com.example.baozi_list.base

import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


abstract class BaseBindingFragment<T : ViewBinding> : Fragment() {
    private var _binding: T? = null
    protected val binding get() = _binding!!

    protected lateinit var inflater: LayoutInflater
    protected var container: ViewGroup?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BindingHelper.getBindingView(javaClass, layoutInflater)
        this.inflater=inflater
        this.container=container
        initView()
        return binding.root
    }


    protected fun <T : View> Int.getView(): T {
        return binding.root.findViewById(this)
    }


    protected fun <VB : ViewBinding> getOtherBinding(bind: Class<VB>): VB {
        val method = bind.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        return method.invoke(
            null,
            inflater,
            container,
            false
        ) as VB
    }


    abstract fun initView()

    protected fun getScreenSize(): Point {
        val resources = this.resources
        val dm = resources.displayMetrics
        val screenWidth = dm.widthPixels
        val screenHeight = dm.heightPixels
        return Point(screenWidth, screenHeight)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
