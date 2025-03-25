package com.example.baozi_list.ui.adapter.binder

import android.graphics.Paint
import android.widget.TextView
import com.example.baozi_list.R
import com.example.baozi_list.bean.Car
import com.example.baozi_list.databinding.DetailLayoutBinding
import com.example.baozi_list.databinding.TaskFoldBinding
import com.example.baozi_list.databinding.TaskUnfoldBinding
import com.example.baozi_list.ui.adapter.viewholders.DetailViewHolder
import com.example.baozi_list.ui.adapter.viewholders.FoldViewHolder
import com.example.baozi_list.ui.adapter.viewholders.UnFoldViewHolder

object ViewHolderBinder {

    fun bindFoldViewHolder(holder: FoldViewHolder,size:Int,car: Car){
        holder.binding as TaskFoldBinding
        if (size == 1) {
            holder.binding.foldTextView.textSize = 20f
            holder.binding.taskFoldTitle.textSize = 25f
        }

        if (car.isFinish) {
            holder.binding.taskFoldTitle.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.taskFoldFinishCheckbox.isChecked = true
            holder.binding.foldTextView.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.taskFoldTitle.text = "车号： ${car.trainNumber}"
            holder.binding.foldTextView.text = "股道： ${car.pickUpLane}"
            holder.binding.taskFoldTime.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.taskFoldTime.text = "时间： ${car.inTime}"
        } else {
            holder.binding.taskFoldTitle.paint.flags = 0
            holder.binding.taskFoldFinishCheckbox.isChecked = false
            holder.binding.foldTextView.paint.flags = 0
            holder.binding.taskFoldTime.paint.flags = 0
            holder.binding.taskFoldTime.text = "时间： ${car.inTime}"
            holder.binding.taskFoldTitle.text = "车号： ${car.trainNumber}"
            holder.binding.foldTextView.text = "股道： ${car.pickUpLane}"
        }
    }

    fun bindUnfoldViewHolder(holder: UnFoldViewHolder, size:Int, car: Car){
        holder.binding as TaskUnfoldBinding
        if (size == 1) {
            holder.binding.unfoldTextView.textSize = 20f
            holder.binding.taskUnfoldTitle.textSize = 25f
        }
        val view = holder.binding.taskUnfoldDetail.getChildAt(0)
        val name = view.findViewById<TextView>(R.id.task_unfold_task_title)
        name.text = "任务： ${car.outTime}"
        name.textSize = 20f
        if (car.isFinish) {
            holder.binding.taskUnfoldTitle.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.taskUnfoldFinishCheckbox.isChecked = true
            holder.binding.unfoldTextView.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.taskUnfoldTime.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.taskUnfoldTime.text = "时间： ${car.inTime}"
            holder.binding.taskUnfoldTitle.text =
                "车号： ${car.trainNumber}"
            holder.binding.unfoldTextView.text = "股道： ${car.pickUpLane}"

        } else {
            holder.binding.taskUnfoldTitle.paint.flags = 0
            holder.binding.taskUnfoldFinishCheckbox.isChecked = false
            holder.binding.unfoldTextView.paint.flags = 0
            holder.binding.taskUnfoldTime.paint.flags = 0
            holder.binding.taskUnfoldTime.text = "时间： ${car.inTime}"
            holder.binding.taskUnfoldTitle.text =
                "车号： ${car.trainNumber}"
            holder.binding.unfoldTextView.text = "股道： ${car.pickUpLane}"
        }
    }

    fun bindDetailViewHolder(holder: DetailViewHolder, car: Car){
        holder.binding as DetailLayoutBinding

        holder.binding.detailBackTrain.text =
            "回库车次： ${car.backTrain}"
        holder.binding.detailTrainInTime.text =
            "入段线时间： ${car.inTime}"
        holder.binding.detailTrainOutTime.text =
            "出段线时间： ${car.outTime}"
        holder.binding.detailTrainPickUpLane.text =
            "接车股道： ${car.pickUpLane}"
        holder.binding.detailTrainNumber.text =
            "车号： ${car.trainNumber}"
    }


}