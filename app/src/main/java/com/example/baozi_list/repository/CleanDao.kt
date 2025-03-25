package com.example.baozi_list.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.baozi_list.bean.Car
import com.example.baozi_list.bean.Clean
import kotlinx.coroutines.flow.Flow


@Dao
interface CleanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClean(clean: Clean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCleans(cleans: List<Clean>)

    @Query("SELECT * FROM clean ORDER BY id DESC LIMIT :pageSize OFFSET :offset")
    fun getCleans(pageSize: Int, offset: Int): Flow<List<Clean>>

    @Query("SELECT * FROM clean WHERE date = :date ORDER BY id DESC")
    fun getTodayCleans(date: String): Flow<List<Clean>>

    @Query("SELECT date FROM clean GROUP BY date ORDER BY id DESC")
    fun getAllDate(): Flow<List<String>>

    @Query("SELECT * FROM clean WHERE car_number = :trainNumber ORDER BY id DESC")
    fun searchClean(trainNumber: String): Flow<List<Clean>>

    @Query("DELETE FROM clean WHERE date=:date and car_number=:carNumber and lane_number=:laneNumber and multi_data=:multiData")
    fun deleteClean(date: String,carNumber: String,laneNumber: String,multiData:String,)

    @Update
    suspend fun updateClean(clean: Clean)

    @Delete
    suspend fun clearTodayData(cleans: List<Clean>)


}