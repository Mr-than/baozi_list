package com.example.baozi_list.ui.adapter.viewholders

import android.content.Context
import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.baozi_list.bean.Car
import com.example.baozi_list.bean.CarManager
import com.example.baozi_list.databinding.TaskFoldBinding
import com.example.baozi_list.ui.MainActivity
import com.example.baozi_list.ui.UpdateDialog
import com.example.baozi_list.ui.view.ScrollerDownView

class FoldViewHolder(
    val binding: ViewBinding,
    oldList: () -> List<Car>,
    private val refresh: (ArrayList<Car>) -> Unit,
    private val updateCar: (Car) -> Unit,
    private val deleteCar:(Car)->Unit,
    recyclerView: RecyclerView,
    context: Context
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
    private val oldDataList = oldList()
    init {
        val binding = binding as TaskFoldBinding
        binding.root.setRecycleViewListener(recyclerView)
        (recyclerView.parent.parent as? ScrollerDownView)?.let {
            binding.root.setDownViewListener(it)
        }
        binding.root.setOnClickListener(this)
        binding.foldDelete.setOnClickListener(this)

        binding.root.setOnLongClickListener {
            (context as? MainActivity)?.let {
                val update = UpdateDialog(oldDataList[adapterPosition])
                update.show(it.supportFragmentManager, "update")
            }
            false
        }

        binding.taskFoldFinishCheckbox.setOnCheckedChangeListener { _, isChecked ->
            setStrikeThrough(isChecked, binding)
            val v = ArrayList<Car>(oldDataList)
            v[adapterPosition].isFinish = isChecked
            refresh(v)
            updateCar(v[adapterPosition])
        }
    }

    private fun setStrikeThrough(isChecked: Boolean, binding: TaskFoldBinding) {
        if (isChecked) {
            binding.taskFoldTitle.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.foldTextView.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.taskFoldTime.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG

            binding.taskFoldTitle.text = "车号： ${oldDataList[adapterPosition].trainNumber}"
            binding.foldTextView.text = "股道： ${oldDataList[adapterPosition].pickUpLane}"
            binding.taskFoldTime.text = "时间： ${oldDataList[adapterPosition].inTime}"
        } else {
            binding.taskFoldTitle.paint.flags = 0
            binding.foldTextView.paint.flags = 0
            binding.taskFoldTime.paint.flags = 0
            binding.taskFoldTitle.text = "车号： ${oldDataList[adapterPosition].trainNumber}"
            binding.foldTextView.text = "股道： ${oldDataList[adapterPosition].pickUpLane}"
            binding.taskFoldTime.text = "时间： ${oldDataList[adapterPosition].inTime}"
        }
    }


    override fun onClick(v: View) {
        val binding = binding as TaskFoldBinding
        when(v.id){
            binding.root.id->onTaskFoldLayoutClick()
            binding.foldDelete.id->onFoldDeleteClick()
        }
    }

    private fun onFoldDeleteClick() {
        val position = adapterPosition
        val l = ArrayList<Car>(oldDataList)
        if (l[position].foldDetail) {
            l.removeAt(position + 1)
        }
        deleteCar(l[position])
        l.removeAt(position)
        refresh(l)
    }

    private fun onTaskFoldLayoutClick() {
        val position = adapterPosition
        if (!oldDataList[position].foldDetail) {
            val car = CarManager.getDetailBean()
            val l = ArrayList<Car>(oldDataList)
            l[position].foldDetail = true
            l.add(position + 1, car)
            refresh(l)
        } else {
            val l = ArrayList<Car>(oldDataList)

            if(l[position+1].isDetail)
                l.removeAt(position + 1)
            l[position].foldDetail = false
            refresh(l)
        }
    }
}