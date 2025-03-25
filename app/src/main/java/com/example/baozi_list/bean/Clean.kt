package com.example.baozi_list.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clean")
data class Clean(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "car_number") val carNumber: String,
    @ColumnInfo(name = "lane_number") val laneNumber: String,
    @ColumnInfo(name = "multi_data") val multiData:String,
    @ColumnInfo(name = "is_tail") var isTail: Boolean,
    @ColumnInfo(name = "is_head") var isHead: Boolean,
    @ColumnInfo(name = "is_divider") var isDivider: Boolean,
):Bean()
