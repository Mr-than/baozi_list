package com.example.baozi_list.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseMulHolderRvAdapter<VH : RecyclerView.ViewHolder> (private val itemSize:Int):
    RecyclerView.Adapter<VH>() {

    override fun getItemCount(): Int = itemSize
    abstract override fun getItemViewType(position: Int): Int

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
    protected fun <BD: ViewBinding> getBinding(parent: ViewGroup, bindingClass:Class<BD>): BD {
        val method = bindingClass.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        return method.invoke(null, LayoutInflater.from(parent.context), parent, false) as BD
    }
}