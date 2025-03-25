package com.example.baozi_list.ui.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.baozi_list.base.BaseMulHolderRvAdapter
import com.example.baozi_list.bean.Car
import com.example.baozi_list.bean.CarManager
import com.example.baozi_list.databinding.DetailLayoutBinding
import com.example.baozi_list.databinding.TaskFoldBinding
import com.example.baozi_list.databinding.TaskUnfoldBinding
import com.example.baozi_list.ui.HistoryViewModel
import com.example.baozi_list.ui.adapter.binder.ViewHolderBinder
import com.example.baozi_list.ui.adapter.viewholders.DetailViewHolder
import com.example.baozi_list.ui.adapter.viewholders.FoldViewHolder
import com.example.baozi_list.ui.adapter.viewholders.UnFoldViewHolder
import com.example.baozi_list.util.CarAdapterCallBack

class HistoryCarAdapter(
    private val recyclerView: RecyclerView,
    itemSize: Int,
    private val size: Int = 0,
    private val viewModel: HistoryViewModel,
    private val context: Context
) : BaseMulHolderRvAdapter<RecyclerView.ViewHolder>(itemSize) {
    private var newDataList: ArrayList<Car> = ArrayList()
    private var oldDataList: ArrayList<Car> = ArrayList()

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return oldDataList.size
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
                    context
                )
            }

            CarManager.DETAIL -> {
                DetailViewHolder(getBinding(parent, DetailLayoutBinding::class.java))
            }

            else -> {
                UnFoldViewHolder(
                    getBinding(parent, TaskUnfoldBinding::class.java),
                    { oldDataList },
                    { refresh(it) },
                    { viewModel.updateCar(it) },
                    { viewModel.deleteCar(it) },
                    recyclerView,
                    context
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
        }
    }

    fun refresh(data: ArrayList<Car>) {
        newDataList = data
        val diffResult = DiffUtil.calculateDiff(CarAdapterCallBack({ oldDataList }, { newDataList }))
        oldDataList.clear()
        oldDataList.addAll(newDataList)
        diffResult.dispatchUpdatesTo(this)
    }


}