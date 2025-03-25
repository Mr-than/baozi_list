package com.example.baozi_list.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter

abstract class BaseVPBindingVMActivity<F : ViewModel, E : FragmentStateAdapter, T : ViewBinding>(
    fragmentList: List<Fragment>?=null
) :
    BaseVP2AndBNVActivity<E, T>(fragmentList) {

    protected lateinit var viewModel: F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelHelper.getViewModel(javaClass, this)
    }
}