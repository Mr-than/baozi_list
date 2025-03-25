package com.example.baozi_list.ui.adapter

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.baozi_list.base.BaseBindingRvAdapter
import com.example.baozi_list.databinding.LogRvItemBinding


class TopRvAdapter(size: Int) : BaseBindingRvAdapter<TopRvAdapter.ViewHolder, LogRvItemBinding>(
    LogRvItemBinding::class.java,
    size
) {

    val list = listOf(
        Color.RED,
        Color.BLUE,
        Color.GREEN,
        Color.YELLOW,
        Color.CYAN,
        Color.MAGENTA,
        Color.LTGRAY,
        Color.DKGRAY,
        Color.BLACK,
        Color.WHITE
    )

    class ViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            val binding = binding as LogRvItemBinding


        }


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding as LogRvItemBinding
        binding.logRvItemTv.text = "$position\n"

    }
}