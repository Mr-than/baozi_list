package com.example.baozi_list.util

import androidx.recyclerview.widget.DiffUtil
import com.example.baozi_list.bean.Car

class CarAdapterCallBack(private val oldList:()->List<Car>, private val newList:()->List<Car>): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList()[oldItemPosition].trainNumber == newList()[newItemPosition].trainNumber
    }

    override fun getOldListSize(): Int {
        val size=oldList().size
        return size
    }

    override fun getNewListSize(): Int {
        val size=newList().size
        return size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldDataList=oldList()
        val newDataList=newList()
        return (oldDataList[oldItemPosition].backTrain == newDataList[newItemPosition].backTrain &&
                oldDataList[oldItemPosition].trainNumber == newDataList[newItemPosition].trainNumber &&
                oldDataList[oldItemPosition].inTime == newDataList[newItemPosition].inTime &&
                oldDataList[oldItemPosition].outTime == newDataList[newItemPosition].outTime &&
                oldDataList[oldItemPosition].pickUpLane == newDataList[newItemPosition].pickUpLane &&
                oldDataList[oldItemPosition].addTime == newDataList[newItemPosition].addTime &&
                oldDataList[oldItemPosition].foldDetail == newDataList[newItemPosition].foldDetail &&
                oldDataList[oldItemPosition].isDetail == newDataList[newItemPosition].isDetail &&
                oldDataList[oldItemPosition].isTime == newDataList[newItemPosition].isTime &&
                oldDataList[oldItemPosition].isFinish == newDataList[newItemPosition].isFinish&&
                oldDataList[oldItemPosition].isTail  == newDataList[newItemPosition].isTail
                )
    }



}