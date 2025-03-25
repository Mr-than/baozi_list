package com.example.baozi_list.ui

import android.content.Context
import android.os.Bundle
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baozi_list.R
import com.example.baozi_list.base.BaseBindingWithVMActivity
import com.example.baozi_list.bean.Car
import com.example.baozi_list.bean.CarManager
import com.example.baozi_list.databinding.ActivitySearchBinding
import com.example.baozi_list.ui.adapter.SearchCarAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchActivity : BaseBindingWithVMActivity<SearchViewModel,ActivitySearchBinding>() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch(Dispatchers.Main) {
            delay(1000)
            binding.activitySearchSearch .requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.activitySearchSearch, InputMethodManager.SHOW_IMPLICIT)
        }



        val adapter = SearchCarAdapter(binding.activitySearchRecyclerView, 0, viewModel = viewModel, context = this)

        val layoutManager = LinearLayoutManager(this)

        binding.activitySearchRecyclerView .adapter = adapter
        binding.activitySearchRecyclerView.layoutManager = layoutManager


        binding.activitySearchSearch.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: android.text.Editable?) {
                if(s.toString().isNotEmpty()){
                    viewModel.searchCar(s.toString())
                }
            }

        })

        viewModel.searchLiveData.observe(this){
            if(it.isNotEmpty()){
                val finalList = ArrayList<Car>()
                var time = ""

                for (i in 0 until it.size) {
                    if (it[i].addTime != time&&it[i].addTime.isNotEmpty()) {
                        time = it[i].addTime
                        finalList.add(
                           CarManager.getTimeBean(time)
                        )
                        finalList.add(it[i])
                    } else {
                        finalList.add(it[i])
                    }
                }
                adapter.refresh(finalList)
            }
        }


    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

        val v: View? = currentFocus
        v?.let {
            if (isShouldHideInput(v, ev)) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }

        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (window.superDispatchTouchEvent(ev)) {
            return true
        }
        return onTouchEvent(ev)
    }

    private fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && (v is EditText)) {
            val leftTop = intArrayOf(0, 0)

            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        return false
    }


}