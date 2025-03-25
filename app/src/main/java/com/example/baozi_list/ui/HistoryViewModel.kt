package com.example.baozi_list.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baozi_list.base.APP
import com.example.baozi_list.bean.Car
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel:ViewModel() {
    private val dao= APP.getApp().database.getCarDao()

    private val _dateListLiveData: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val dateListLiveData: LiveData<ArrayList<String>> get() = _dateListLiveData

    private val _carsLiveData:MutableLiveData<ArrayList<Car>> =MutableLiveData()
    val carsLiveData:LiveData<ArrayList<Car>> get() = _carsLiveData


    fun getHistoryData() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = ArrayList<String>()
            dao.getAllDate()
                .flowOn(Dispatchers.IO)
                .map {
                    list.addAll(it)
                    list
                }.first()
            _dateListLiveData.postValue(list)
        }
    }

    private suspend fun getCars(date:String): ArrayList<Car> {
        return withContext(Dispatchers.IO) {
            val list = ArrayList<Car>()
            dao.getTodayCars(date)
                .flowOn(Dispatchers.IO)
                .map {
                    list.addAll(it)
                    list
                }.first()
        }
    }

    fun getDateCars(time:String){
        viewModelScope.launch(Dispatchers.IO) {
            val list = getCars(time)
            _carsLiveData.postValue(list)
        }
    }


    fun updateCar(car:Car){
        viewModelScope.launch(Dispatchers.IO) {
            car.foldDetail=false
            dao.updateCar(car)
        }
    }

    fun deleteCar(car: Car) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteCar(car.trainNumber,car.backTrain,car.inTime,car.outTime,car.pickUpLane,car.addTime)
        }
    }

}