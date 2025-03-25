package com.example.baozi_list.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.baozi_list.util.getGenericsClass

object ViewModelHelper {

    private val VIEWMODEL_CLASS = HashMap<Class<*>, Class<*>>()

    fun <E : ViewModel> getViewModel(componentClazz: Class<*>, activity: ViewModelStoreOwner): E {
        return ViewModelProvider(activity)[getViewModelClass(componentClazz)]
    }

    private fun <E : ViewModel> getViewModelClass(componentClazz: Class<*>): Class<E> {

        if (VIEWMODEL_CLASS.contains(componentClazz)) {
            return VIEWMODEL_CLASS[componentClazz] as Class<E>
        }
        return getGenericsClass<ViewModel, E>(componentClazz).let {
            VIEWMODEL_CLASS[componentClazz] = it
            it
        }
    }

}