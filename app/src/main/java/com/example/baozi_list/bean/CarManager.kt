package com.example.baozi_list.bean

import com.example.baozi_list.util.getDate

object CarManager {

    const val FOLD = 1
    const val UNFOLD = 2
    const val DETAIL = 3
    const val TIME = 4
    const val TAIL = 5


    fun getTailBean(): Car {
        return Car(
            0,
            "",
            "",
            "",
            "",
            "",
            "",
            foldDetail = false,
            isDetail = false,
            isTime = false,
            isTail = true,
            isFinish = false
        )
    }

    fun getFoldBean(): Car {
        return Car(
            0,
            "",
            "",
            "",
            "",
            "",
            "",
            foldDetail = false,
            isDetail = false,
            isTime = false,
            isTail = false,
            isFinish = false
        )
    }

    fun getUnFoldBean(): Car {
        return Car(
            0,
            "",
            "",
            "",
            "do something",
            "",
            "",
            foldDetail = false,
            isDetail = false,
            isTime = false,
            isTail = false,
            isFinish = false
        )
    }

    fun getDetailBean(): Car {
        return Car(
            0,
            "",
            "",
            "",
            "",
            "",
            "",
            foldDetail = false,
            isDetail = true,
            isTime = false,
            isTail = false,
            isFinish = false
        )
    }

    fun getTimeBean(time: String = getDate()): Car {
        return Car(
            0,
            "",
            "",
            "",
            "",
            "",
            time,
            foldDetail = false,
            isDetail = false,
            isTime = true,
            isTail = false,
            isFinish = false
        )
    }


    fun getBeanType(car: Car): Int {
        return if (car.isTime) {
            TIME
        } else if (car.isTail) {
            TAIL
        } else if (car.isDetail) {
            DETAIL
        } else if (car.outTime.isEmpty()) {
            FOLD
        } else {
            UNFOLD
        }
    }
}