package com.example.baozi_list.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car")
data class Car(
    @PrimaryKey(autoGenerate = true) var id:Int=0,
    @ColumnInfo(name = "train_number") val trainNumber: String,
    @ColumnInfo(name = "back_train") val backTrain: String,
    @ColumnInfo(name = "in_time") val inTime:String,
    @ColumnInfo(name = "out_time") val outTime:String,
    @ColumnInfo(name = "pick_up_lane") val pickUpLane:String,
    @ColumnInfo(name = "add_time") val addTime:String,
    @ColumnInfo(name = "fold_detail") var foldDetail:Boolean,
    @ColumnInfo(name = "is_detail") val isDetail:Boolean,
    @ColumnInfo(name = "is_time") val isTime:Boolean,
    @ColumnInfo(name = "is_tail") val isTail:Boolean,



    @ColumnInfo(name = "is_finish") var isFinish:Boolean=false
):Bean(){
    override fun toString(): String {
        return "车号：$trainNumber 回库车次： $backTrain 入段线时间： $inTime 出段线时间： $outTime 接车股道： $pickUpLane"
    }
}