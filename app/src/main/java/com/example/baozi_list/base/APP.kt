package com.example.baozi_list.base

import android.app.Application
import com.example.baozi_list.repository.CarDatabase


class APP : Application() {

    companion object {
        private lateinit var context: APP
        fun getApp() = context

        const val CAR=0
        const val CLEAN=1
        const val DI=2

        var listType= CAR
    }
    
    val database: CarDatabase by lazy { CarDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        context = this
    }



}