package com.example.baozi_list.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.baozi_list.bean.Disinfection
import kotlinx.coroutines.flow.Flow


@Dao
interface DisinfectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDisinfection(disinfection: Disinfection)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDisinfections(disinfections: List<Disinfection>)


    @Query("SELECT * FROM disinfection ORDER BY id DESC LIMIT :pageSize OFFSET :offset")
    fun getDisinfections(pageSize: Int, offset: Int): Flow<List<Disinfection>>


    @Query("SELECT * FROM disinfection WHERE date = :date ORDER BY id DESC")
    fun getTodayDisinfections(date: String): Flow<List<Disinfection>>

    @Query("SELECT * FROM disinfection WHERE car_number=:carNumber ORDER BY id DESC")
    fun searchDisinfection(carNumber: String): Flow<List<Disinfection>>


    @Query("DELETE FROM disinfection WHERE date=:date and car_number=:carNumber")
    fun deleteDisinfection(date: String, carNumber: String,)

    @Update
    suspend fun updateDisinfection(disinfection: Disinfection)

    @Delete
    suspend fun clearTodayData(disinfection: List<Disinfection>)



}