package com.example.baozi_list.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseBindingVMFragment<E:ViewModel,T:ViewBinding>: BaseBindingFragment<T>(){
    private val displayMetrics by lazy { resources.displayMetrics }
    protected val screenWidth by lazy { displayMetrics.widthPixels }
    protected val screenHeight by lazy { displayMetrics.heightPixels }
    private var _viewModel:E?=null
    protected val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViewModel()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initViewModel(){
        _viewModel = ViewModelHelper.getViewModel(javaClass, requireActivity())
    }
}