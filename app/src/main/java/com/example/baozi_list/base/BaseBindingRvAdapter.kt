package com.example.baozi_list.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.baozi_list.util.getGenericsObject

/**
 * @param VH ViewHolder
 * @param VB ViewBinding
 */
abstract class BaseBindingRvAdapter<VH : RecyclerView.ViewHolder, VB :ViewBinding?>(
    private val layout: Class<VB>,
    private val itemSize:Int
) :
    RecyclerView.Adapter<VH>() {
    override fun getItemCount(): Int = itemSize

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = getBinding(parent)
        return getGenericsObject<RecyclerView.ViewHolder, VH>(
            javaClass,
            mapOf(ViewBinding::class.java to binding)
        )
    }

    protected fun getBinding(parent: ViewGroup): VB {
        val method = layout.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        return method.invoke(null, LayoutInflater.from(parent.context), parent, false) as VB
    }

}