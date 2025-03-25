package com.example.baozi_list.repository

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.baozi_list.base.APP
import com.example.baozi_list.bean.Car
import com.example.baozi_list.bean.Clean
import com.example.baozi_list.bean.Disinfection


@Database(entities = [Car::class,Disinfection::class,Clean::class],version = 1)
abstract class CarDatabase :RoomDatabase(){

    companion object {

        @Volatile
        private var INSTANCE: CarDatabase? = null
        fun getDatabase(context: Context): CarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(APP.getApp(), CarDatabase::class.java, "db_car")
                    //数据库创建和打开后的回调，可以重写其中的方法
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Log.d("===================", "onCreate: database")
                        }
                    })
                    //数据库升级异常之后的回滚
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }

    }

    abstract fun getCarDao(): Dao
    abstract fun getDisinfectionDao(): DisinfectionDao
    abstract fun getCleanDao(): CleanDao

}