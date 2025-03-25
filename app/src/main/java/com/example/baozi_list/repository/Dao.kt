package com.example.baozi_list.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.baozi_list.bean.Car
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: Car)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCars(cars: List<Car>)

    @Query("SELECT * FROM car ORDER BY id DESC LIMIT :pageSize OFFSET :offset")
    fun getCars(pageSize: Int, offset: Int): Flow<List<Car>>

    @Query("SELECT * FROM car WHERE add_time = :date ORDER BY id DESC")
    fun getTodayCars(date: String): Flow<List<Car>>

    @Query("SELECT add_time FROM car GROUP BY add_time ORDER BY id DESC")
    fun getAllDate(): Flow<List<String>>

    @Query("SELECT * FROM car WHERE train_number = :trainNumber ORDER BY id DESC")
    fun searchCar(trainNumber: String): Flow<List<Car>>

    @Query("DELETE FROM car WHERE train_number = :trainNumber and back_train = :backTrain and in_time = :inTime and out_time = :outTime and pick_up_lane = :pickUpLane and add_time = :addTime")
    fun deleteCar(trainNumber:String, backTrain:String, inTime:String, outTime:String, pickUpLane:String, addTime:String)

    @Update
    suspend fun updateCar(car: Car)

    @Delete
    suspend fun clearTodayData(cars: List<Car>)


}