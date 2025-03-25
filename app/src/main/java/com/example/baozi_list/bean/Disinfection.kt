package com.example.baozi_list.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "disinfection")
data class Disinfection(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "car_number")val carNumber: String,
    @ColumnInfo(name = "is_finish") var isFinish:Boolean,
    @ColumnInfo(name = "is_tail") var isTail:Boolean,
    @ColumnInfo(name = "is_head") var isHead:Boolean
):Bean()