package com.example.baozi_list.ui.adapter

import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.baozi_list.base.BaseMulHolderRvAdapter
import com.example.baozi_list.bean.Car
import com.example.baozi_list.bean.CarManager
import com.example.baozi_list.databinding.DetailLayoutBinding
import com.example.baozi_list.databinding.TaskFoldBinding
import com.example.baozi_list.databinding.TaskTailItemBinding
import com.example.baozi_list.databinding.TaskUnfoldBinding
import com.example.baozi_list.databinding.TimeLayoutBinding
import com.example.baozi_list.ui.HistoryActivity
import com.example.baozi_list.ui.MainViewModel
import com.example.baozi_list.ui.adapter.binder.ViewHolderBinder
import com.example.baozi_list.ui.adapter.viewholders.DetailViewHolder
import com.example.baozi_list.ui.adapter.viewholders.FoldViewHolder
import com.example.baozi_list.ui.adapter.viewholders.UnFoldViewHolder
import com.example.baozi_list.util.CarAdapterCallBack
import com.example.baozi_list.util.lastElement

class HomeCarAdapter(
    private val recyclerView: RecyclerView,
    itemSize: Int,
    private val size: Int = 0,
    private val viewModel: MainViewModel
) :
    BaseMulHolderRvAdapter<RecyclerView.ViewHolder>(itemSize) {
    private var newDataList: ArrayList<Car> = ArrayList()
    private var oldDataList: ArrayList<Car> = ArrayList()

    override fun getItemCount(): Int {
        return oldDataList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return CarManager.getBeanType(oldDataList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CarManager.FOLD -> {
                FoldViewHolder(
                    getBinding(parent, TaskFoldBinding::class.java),
                    { oldDataList },
                    { refresh(it) },
                    { viewModel.updateCar(it) },
                    { viewModel.deleteCar(it) },
                    recyclerView,
                    parent.context
                )
            }

            CarManager.DETAIL -> {
                DetailViewHolder(getBinding(parent, DetailLayoutBinding::class.java))
            }

            CarManager.TIME -> {
                TimeViewHolder(getBinding(parent, TimeLayoutBinding::class.java))
            }

            CarManager.TAIL -> {
                TailViewHolder(getBinding(parent, TaskTailItemBinding::class.java))
            }

            else -> {
                UnFoldViewHolder(
                    getBinding(parent, TaskUnfoldBinding::class.java),
                    { oldDataList },
                    { refresh(it) },
                    { viewModel.updateCar(it) },
                    {},
                    recyclerView,
                    parent.context
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FoldViewHolder -> {
                holder.binding as TaskFoldBinding
                ViewHolderBinder.bindFoldViewHolder(holder, size, oldDataList[position])
            }

            is UnFoldViewHolder -> {
                holder.binding as TaskUnfoldBinding
                ViewHolderBinder.bindUnfoldViewHolder(holder, size, oldDataList[position])
            }

            is DetailViewHolder -> {
                holder.binding as DetailLayoutBinding
                ViewHolderBinder.bindDetailViewHolder(holder, oldDataList[position - 1])
            }

            is TailViewHolder -> {
                holder.binding as TaskTailItemBinding

                if (oldDataList.size == 1 && oldDataList.lastElement().isTime && oldDataList.lastElement().isDetail) {
                    holder.binding.taskTailClearButton.isClickable = false
                } else {
                    holder.binding.taskTailClearButton.isClickable = true
                }
            }

            is TimeViewHolder -> {
                holder.binding as TimeLayoutBinding
                holder.binding.timeTextView.text = oldDataList[position].addTime
            }
        }
    }

    inner class TailViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            val binding = binding as TaskTailItemBinding
            binding.taskTailButton.setOnClickListener {
                val intent = Intent(binding.root.context, HistoryActivity::class.java)
                binding.root.context.startActivity(intent)
            }
            binding.taskTailClearButton.setOnClickListener {
                val l = ArrayList<Car>()
                for (i in oldDataList) {
                    if (!i.isTime && !i.isDetail) {
                        l.add(i)
                    }
                }
                viewModel.clearTodayData(l)
                refresh(ArrayList<Car>().apply {
                    add(
                        CarManager.getTailBean()
                    )
                })
            }
        }
    }

    inner class TimeViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    fun refresh(data: ArrayList<Car>) {
        newDataList = data
        if (size != 1) {
            if (newDataList.size > 0 && !(newDataList.lastElement().isTail)) {
                newDataList.add(
                    CarManager.getTailBean()
                )
            } else if (newDataList.size == 0) {
                newDataList.add(
                    CarManager.getTailBean()
                )
            }
        }
        val diffResult =
            DiffUtil.calculateDiff(CarAdapterCallBack({ oldDataList }, { newDataList }))
        oldDataList.clear()
        oldDataList.addAll(newDataList)
        diffResult.dispatchUpdatesTo(this)
    }
}