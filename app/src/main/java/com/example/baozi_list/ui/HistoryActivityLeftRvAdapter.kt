package com.example.baozi_list.ui

import android.graphics.Color
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.baozi_list.R
import com.example.baozi_list.base.BaseBindingRvAdapter
import com.example.baozi_list.databinding.ActivityHistoryLeftItemBinding

class HistoryActivityLeftRvAdapter(private val dataList: ArrayList<String>) :
    BaseBindingRvAdapter<HistoryActivityLeftRvAdapter.CommunityActivityViewHolder, ActivityHistoryLeftItemBinding>(
        ActivityHistoryLeftItemBinding::class.java,
        dataList.size
    ) {

    private val list = mutableListOf<Boolean>()

    init {
        list.addAll(dataList.map { false })
        if (list.isNotEmpty())
            list[0]=true
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityActivityViewHolder {
        return CommunityActivityViewHolder(getBinding(parent),list, ::notifyDataSetChanged)
    }

    class CommunityActivityViewHolder(val binding: ViewBinding,val list: MutableList<Boolean>, val block: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            val binding = binding as ActivityHistoryLeftItemBinding
            binding.dateLeftItem .setOnClickListener {
                val index = layoutPosition
                list[index] = true
                binding.dateLeftItem.setBackgroundResource(R.color.white)
                for (i in list.indices) {
                    if (i != index) {
                        list[i] = false
                    }
                }
                block()
            }
        }

        fun autoClick(){
            val binding = binding as ActivityHistoryLeftItemBinding
            binding.dateLeftItem.performClick()
        }
    }

    override fun onBindViewHolder(holder: CommunityActivityViewHolder, position: Int) {
        val binding = holder.binding as ActivityHistoryLeftItemBinding

        binding.dateLeftItem.text = dataList[position]

        if (list[position]) {
            binding.dateLeftItem.setBackgroundResource(R.color.white)
        } else {
            binding.dateLeftItem.setBackgroundColor(Color.parseColor("#f5f5f5"))
        }
    }


    fun getListItem(position: Int): String {
        return dataList[position]
    }


}