package com.example.baozi_list.util

import androidx.recyclerview.widget.DiffUtil
import com.example.baozi_list.bean.Disinfection

class DisinfectionAdapterCallBack(
    private val oldList: () -> List<Disinfection>,
    private val newList: () -> List<Disinfection>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList()[oldItemPosition].carNumber == newList()[newItemPosition].carNumber
    }

    override fun getOldListSize(): Int {
        val size = oldList().size
        return size
    }

    override fun getNewListSize(): Int {
        val size = newList().size
        return size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldDataList = oldList()
        val newDataList = newList()
        return (
                oldDataList[oldItemPosition].carNumber == newDataList[newItemPosition].carNumber &&
                        oldDataList[oldItemPosition].date == newDataList[newItemPosition].date &&
                        oldDataList[oldItemPosition].isHead == newDataList[newItemPosition].isHead &&
                        oldDataList[oldItemPosition].isTail == newDataList[newItemPosition].isTail &&
                        oldDataList[oldItemPosition].isFinish == newDataList[newItemPosition].isFinish
                )
    }
}