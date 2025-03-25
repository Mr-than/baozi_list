package com.example.baozi_list.bean

object DisinfectionManager {
    const val ITEM = 3
    const val TIME = 4
    const val TAIL = 5


    fun getBeanType(disinfection: Disinfection): Int {
        return if(disinfection.isTail){
            return TAIL
        }else if (disinfection.isHead){
            return TIME
        }else{
            return ITEM
        }
    }

}