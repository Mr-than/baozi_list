package com.example.baozi_list.ui.adapter.viewholders

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.baozi_list.R
import com.example.baozi_list.bean.Car
import com.example.baozi_list.bean.CarManager
import com.example.baozi_list.databinding.TaskUnfoldBinding
import com.example.baozi_list.ui.MainActivity
import com.example.baozi_list.ui.UpdateDialog
import com.example.baozi_list.ui.view.ScrollerDownView

class UnFoldViewHolder(
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

        val binding = binding as TaskUnfoldBinding

        binding.root.setRecycleViewListener(recyclerView)
        (recyclerView.parent.parent as? ScrollerDownView)?.let {

            binding.root.setDownViewListener(it)
        }

        binding.root.setOnClickListener(this)

        binding.root.setOnLongClickListener {
            (context as? MainActivity)?.let {
                val update = UpdateDialog(oldDataList[adapterPosition])
                update.show(it.supportFragmentManager, "update")
            }
            false
        }

        binding.unfoldDelete.setOnClickListener(this)

        val view =
            LayoutInflater.from(binding.root.context).inflate(R.layout.task_unfold_task_item, null)
        val name = view.findViewById<TextView>(R.id.task_unfold_task_title)

        binding.taskUnfoldFinishCheckbox.setOnCheckedChangeListener { _, isChecked ->
            setStrikeThrough(isChecked, binding)

            val v = ArrayList<Car>(oldDataList)
            v[adapterPosition].isFinish = isChecked
            refresh(v)
            updateCar
        }
        name.textSize = 20f
        binding.taskUnfoldDetail.addView(view)
    }
    private fun setStrikeThrough(isChecked: Boolean, binding: TaskUnfoldBinding) {
        if (isChecked) {
            binding.taskUnfoldTitle.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.taskUnfoldTitle.text = "任务"
            binding.unfoldTextView.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.taskUnfoldTime.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG

            binding.taskUnfoldTime.text = "时间： ${oldDataList[adapterPosition].inTime}"
            binding.taskUnfoldTitle.text =
                "车号： ${oldDataList[adapterPosition].trainNumber}"
            binding.unfoldTextView.text = "股道： ${oldDataList[adapterPosition].pickUpLane}"
        } else {
            binding.taskUnfoldTitle.paint.flags = 0
            binding.taskUnfoldTitle.text = "任务"
            binding.unfoldTextView.paint.flags = 0
            binding.taskUnfoldTime.paint.flags = 0
            binding.taskUnfoldTime.text = "时间： ${oldDataList[adapterPosition].inTime}"
            binding.taskUnfoldTitle.text =
                "车号： ${oldDataList[adapterPosition].trainNumber}"
            binding.unfoldTextView.text = "股道： ${oldDataList[adapterPosition].pickUpLane}"
        }
    }

    override fun onClick(v: View?) {
        val binding = binding as TaskUnfoldBinding
        when(v?.id){
            binding.root.id->onRootClick()
            binding.unfoldDelete.id->onFoldDeleteClick()
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

    private fun onRootClick() {
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