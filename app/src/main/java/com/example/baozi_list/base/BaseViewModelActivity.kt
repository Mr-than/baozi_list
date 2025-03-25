package com.example.baozi_list.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel

abstract class BaseViewModelActivity<E:ViewModel> : AppCompatActivity(){
    protected lateinit var viewModel: E

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    private fun initViewModel(){
        viewModel = ViewModelHelper.getViewModel(javaClass, this)
    }


}