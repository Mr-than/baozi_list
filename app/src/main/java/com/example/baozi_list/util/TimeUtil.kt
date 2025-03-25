package com.example.baozi_list.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


fun getDate(): String {
    val calendar: Calendar = Calendar.getInstance()

    return "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)}"
}

fun getTime(): String {
    val calendar: Calendar = Calendar.getInstance()

    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(calendar.time)
}

fun isEarlyMorning(time: String): Boolean {
    val hourMinute = time.split(":").map { it.toInt() }
    val hour = hourMinute[0]
    val minute = hourMinute[1]

    // 判断是否在凌晨时间段：00:00 到 06:00
    return (hour < 6) || (hour == 6 && minute == 0)
}

fun getPreviousDay(): String {
    val calendar: Calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_MONTH, -1)
    return "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)}"
}
