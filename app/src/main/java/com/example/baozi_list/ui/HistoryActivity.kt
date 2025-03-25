package com.example.baozi_list.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baozi_list.R
import com.example.baozi_list.base.BaseBindingWithVMActivity
import com.example.baozi_list.databinding.ActivityHistoryBinding
import com.example.baozi_list.ui.adapter.HistoryCarAdapter

class HistoryActivity : BaseBindingWithVMActivity<HistoryViewModel, ActivityHistoryBinding>() {

    private var num = 0
    private var data = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val adapter= HistoryCarAdapter(binding.historyDetail,0,viewModel=viewModel, context = this)

        binding.historyDetail.adapter=adapter
        binding.historyDetail.layoutManager=LinearLayoutManager(this)



        viewModel.dateListLiveData.observe(this) {
            val leftAdapter = HistoryActivityLeftRvAdapter(it)
            binding.historyList.adapter = leftAdapter
            binding.historyList.layoutManager = LinearLayoutManager(this)
            data = it
            if(it.isNotEmpty())
            binding.historyList.post {
                val view1: View = binding.historyList.getChildAt(0) //获取到对应Item的View
                val myViewHolder: HistoryActivityLeftRvAdapter.CommunityActivityViewHolder =
                    binding.historyList.getChildViewHolder(view1) as HistoryActivityLeftRvAdapter.CommunityActivityViewHolder
                myViewHolder.autoClick() //调用ViewHolder中的方法


                viewModel.getDateCars(it[0])
                num = 0
            }
        }
        viewModel.getHistoryData()

        viewModel.carsLiveData.observe(this){
            adapter.refresh(it)
        }

        binding.historyList.addOnItemTouchListener(MyListener())


    }
}