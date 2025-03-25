package com.example.baozi_list.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.baozi_list.base.BaseMulHolderRvAdapter
import com.example.baozi_list.bean.Disinfection
import com.example.baozi_list.bean.DisinfectionManager
import com.example.baozi_list.databinding.DisinfectionItemBinding
import com.example.baozi_list.databinding.TaskTailItemBinding
import com.example.baozi_list.databinding.TimeLayoutBinding
import com.example.baozi_list.ui.HistoryActivity
import com.example.baozi_list.ui.MainActivity
import com.example.baozi_list.ui.MainViewModel
import com.example.baozi_list.ui.UpdateDialog
import com.example.baozi_list.ui.view.ScrollerDownView
import com.example.baozi_list.util.DisinfectionAdapterCallBack
import com.example.baozi_list.util.lastElement

class DisinfectionAdapter(
    itemSize: Int,
    private val viewModel: MainViewModel,
    private val size: Int = 0,
    private val recyclerView: RecyclerView,
) :
    BaseMulHolderRvAdapter<RecyclerView.ViewHolder>(itemSize) {

    private var newDataList: ArrayList<Disinfection> = ArrayList()
    private var oldDataList: ArrayList<Disinfection> = ArrayList()

    override fun getItemCount(): Int {
        return oldDataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return DisinfectionManager.getBeanType(oldDataList[position])
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            DisinfectionManager.TIME -> {
                TimeViewHolder(getBinding(parent, TimeLayoutBinding::class.java))
            }

            DisinfectionManager.TAIL -> {
                TailViewHolder(getBinding(parent, TaskTailItemBinding::class.java))
            }

            else -> {
                DisinfectionViewHolder(getBinding(parent, DisinfectionItemBinding::class.java),parent.context)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder){
            is DisinfectionViewHolder -> {
                val binding = holder.binding as DisinfectionItemBinding
                val data = oldDataList[position]
                binding.disinfectionCarNumber.text=data.carNumber
            }
            is TimeViewHolder -> {
                val binding = holder.binding
                binding.timeTextView.text=oldDataList[position].date
            }
        }


    }


    inner class DisinfectionViewHolder(val binding: ViewBinding,context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            val binding = binding as DisinfectionItemBinding
            binding.root.setRecycleViewListener(recyclerView)
            (recyclerView.parent.parent as? ScrollerDownView)?.let {
                binding.root.setDownViewListener(it)
            }
            binding.root.setOnLongClickListener {
                (context as? MainActivity)?.let {
                    val update = UpdateDialog(oldDataList[adapterPosition])
                    update.show(it.supportFragmentManager, "update")
                }
                false
            }
        }
    }

    inner class TimeViewHolder(val binding: TimeLayoutBinding) : RecyclerView.ViewHolder(binding.root)
    inner class TailViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            val binding = binding as TaskTailItemBinding
            binding.taskTailButton .setOnClickListener {
                val intent = Intent(binding.root.context, HistoryActivity::class.java)
                binding.root.context.startActivity(intent)
            }
            binding.taskTailClearButton.setOnClickListener {
                val l = ArrayList<Disinfection>()
                for (i in oldDataList) {
                    if (!i.isHead && !i.isTail) {
                        l.add(i)
                    }
                }
                viewModel.clearTodayDisinfection(l)
                refresh(ArrayList<Disinfection>().apply {
                    add(
                        Disinfection(
                            0,
                            "",
                            "",
                            isFinish = false,
                            isTail = true,
                            isHead = false
                        )
                    )
                })
            }
        }
    }

    fun refresh(data: ArrayList<Disinfection>) {
        newDataList = data
        if (size != 1) {
            if (newDataList.size > 0 && !(newDataList.lastElement().isTail)) {
                newDataList.add(
                    Disinfection(
                        0,
                        "",
                        "",
                        isFinish = false,
                        isTail = true,
                        isHead = false
                    )
                )
            } else if (newDataList.size == 0) {
                newDataList.add(
                    Disinfection(
                        0,
                        "",
                        "",
                        isFinish = false,
                        isTail = true,
                        isHead = false
                    )
                )
            }
        }
        val diffResult =
            DiffUtil.calculateDiff(DisinfectionAdapterCallBack({ oldDataList }, { newDataList }))
        oldDataList.clear()
        oldDataList.addAll(newDataList)
        diffResult.dispatchUpdatesTo(this)
    }

}