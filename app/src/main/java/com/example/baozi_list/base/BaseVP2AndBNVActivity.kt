package com.example.baozi_list.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.baozi_list.util.getGenericsObject
import com.google.android.material.bottomnavigation.BottomNavigationView

abstract class BaseVP2AndBNVActivity<E : FragmentStateAdapter, T : ViewBinding>(private val fragmentList: List<Fragment>?=null) :
    BaseBindingActivity<T>() {

    protected val viewPager by lazy { getViewPager2() }
    protected val bnv by lazy { getBottomNavigationView() }
    protected val bnvItemList by lazy { mutableListOf<Int>() }

    private var _isAnimator: Boolean = true
    protected var isAnimator: Boolean
        get() = _isAnimator
        set(value) {
            _isAnimator = value
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBottomNavigationViewMenusId()
        initView()

    }

    protected fun setFragmentList(fragmentList: List<Fragment>){
        viewPager.adapter = getGenericsObject<FragmentStateAdapter, E>(
            javaClass,
            mapOf(List::class.java to fragmentList, AppCompatActivity::class.java to this)
        )
    }

    private fun initView() {

        if(fragmentList!=null){
            setFragmentList(fragmentList)
        }

        bnv.setOnItemSelectedListener { item ->
            viewPager.setCurrentItem(bnvItemList.indexOf(item.itemId), _isAnimator)
            true
        }
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bnv.selectedItemId = bnvItemList[position]
            }
        })
    }


    private fun getViewPager2(): ViewPager2 {
        val viewPager2 = getBindingFieldByType(ViewPager2::class.java)
        return viewPager2 ?: throw RuntimeException("需要一个ViewPager2")
    }

    private fun getBottomNavigationView(): BottomNavigationView {
        val viewPager2 = getBindingFieldByType(BottomNavigationView::class.java)
        return viewPager2 ?: throw RuntimeException("需要一个BottomNavigationView")
    }

    private fun getBottomNavigationViewMenusId() {
        bnv.menu.forEach {
            bnvItemList.add(it.itemId)
        }
    }


    protected fun <T> getBindingFieldByType(type: Class<T>): T? {
        for (field in binding.javaClass.fields) {
            if (type.isAssignableFrom(field.type)) {
                return field.get(binding) as T
            }
        }
        return null
    }


    protected fun setViewPager(block: ViewPager2.() -> Unit) {
        block(viewPager)
    }

    protected fun setBottomNavigationView(block: BottomNavigationView.() -> Unit) {
        block(bnv)
    }


}