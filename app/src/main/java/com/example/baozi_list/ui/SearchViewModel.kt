package com.example.baozi_list.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baozi_list.base.APP
import com.example.baozi_list.bean.Car
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
    private val dao= APP.getApp().database.getCarDao()
    private val _searchLiveData: MutableLiveData<ArrayList<Car>> = MutableLiveData()
    val searchLiveData: LiveData<ArrayList<Car>> get() = _searchLiveData

    fun searchCar(trainNumber:String){



        viewModelScope.launch(Dispatchers.IO) {
            dao.searchCar(trainNumber)
                .flowOn(Dispatchers.IO)
                .map {
                    val list=ArrayList<Car>()
                    list.addAll(it)
                    list
                }
                .collect {
                    _searchLiveData.postValue(it)
                }
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