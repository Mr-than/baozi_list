package com.example.baozi_list.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseBindingWithVMActivity<E:ViewModel,T:ViewBinding>: BaseBindingActivity<T>(){

    protected lateinit var viewModel: E


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    private fun initViewModel(){
        viewModel = ViewModelHelper.getViewModel(javaClass, this)
    }

}