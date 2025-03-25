package com.example.baozi_list.bean

import com.example.baozi_list.bean.CarManager.DETAIL
import com.example.baozi_list.bean.CarManager.FOLD
import com.example.baozi_list.bean.CarManager.TAIL
import com.example.baozi_list.bean.CarManager.TIME
import com.example.baozi_list.bean.CarManager.UNFOLD

object CleanManager {
    const val ITEM = 3
    const val TIME = 4
    const val TAIL = 5
    const val DIVIDER = 6

    fun getBeanType(clean: Clean): Int {
        return if (clean.isHead) {
            TIME
        } else if (clean.isTail) {
            TAIL
        } else if(clean.isDivider){
            DIVIDER
        } else {
            ITEM
        }
    }

}