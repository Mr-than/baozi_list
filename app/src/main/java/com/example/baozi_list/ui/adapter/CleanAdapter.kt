package com.example.baozi_list.ui.adapter

import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.baozi_list.base.BaseMulHolderRvAdapter
import com.example.baozi_list.bean.Car
import com.example.baozi_list.bean.CarManager
import com.example.baozi_list.bean.Clean
import com.example.baozi_list.bean.CleanManager
import com.example.baozi_list.databinding.CleanContrastRvTypeItemBinding
import com.example.baozi_list.databinding.CleanItemBinding
import com.example.baozi_list.databinding.TaskTailItemBinding
import com.example.baozi_list.databinding.TimeLayoutBinding
import com.example.baozi_list.ui.HistoryActivity
import com.example.baozi_list.ui.MainViewModel
import com.example.baozi_list.util.CarAdapterCallBack
import com.example.baozi_list.util.CleanAdapterCallBack
import com.example.baozi_list.util.lastElement

class CleanAdapter(itemSize: Int, private val size: Int = 0, private val viewModel: MainViewModel) :
    BaseMulHolderRvAdapter<RecyclerView.ViewHolder>(itemSize) {
    private var newDataList: ArrayList<Clean> = ArrayList()
    private var oldDataList: ArrayList<Clean> = ArrayList()


    override fun getItemCount(): Int {
        return oldDataList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return CleanManager.getBeanType(oldDataList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CleanManager.TIME -> {
                TimeViewHolder(getBinding(parent, TimeLayoutBinding::class.java))
            }

            CleanManager.TAIL -> {
                TailViewHolder(getBinding(parent, TaskTailItemBinding::class.java))
            }

            CleanManager.DIVIDER -> {
                DividerViewHolder(getBinding(parent, CleanContrastRvTypeItemBinding::class.java))
            }

            else -> {
                CleanViewHolder(getBinding(parent, CleanItemBinding::class.java))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


    }

    class CleanViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            val binding = binding as CleanItemBinding


        }
    }

    inner class DividerViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)
    inner class TimeViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)
    inner class TailViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            val binding = binding as TaskTailItemBinding
            binding.taskTailButton.setOnClickListener {
                val intent = Intent(binding.root.context, HistoryActivity::class.java)
                binding.root.context.startActivity(intent)
            }
            binding.taskTailClearButton.setOnClickListener {
                val l = ArrayList<Clean>()
                for (i in oldDataList) {
                    if (!i.isHead && !i.isTail&& !i.isDivider) {
                        l.add(i)
                    }
                }
                viewModel.clearTodayClean(l)
                refresh(ArrayList<Clean>().apply {
                    add(
                        Clean(
                            0,
                            "",
                            "",
                            "",
                            "",
                            isTail = true,
                            isHead = false,
                            isDivider = false
                        )
                    )
                })
            }
        }
    }

    fun refresh(data: ArrayList<Clean>) {
        newDataList = data
        if (size != 1) {
            if (newDataList.size > 0 && !(newDataList.lastElement().isTail)) {
                newDataList.add(
                    Clean(
                        0,
                        "",
                        "",
                        "",
                        "",
                        isTail = true,
                        isHead = false,
                        isDivider = false
                    )
                )
            } else if (newDataList.size == 0) {
                newDataList.add(
                    Clean(
                        0,
                        "",
                        "",
                        "",
                        "",
                        isTail = true,
                        isHead = false,
                        isDivider = false

                    )
                )
            }
        }
        val diffResult =
            DiffUtil.calculateDiff(CleanAdapterCallBack({ oldDataList }, { newDataList }))
        oldDataList.clear()
        oldDataList.addAll(newDataList)
        diffResult.dispatchUpdatesTo(this)
    }

}