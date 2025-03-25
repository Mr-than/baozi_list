package com.example.baozi_list.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baozi_list.base.APP
import com.example.baozi_list.bean.Car
import com.example.baozi_list.bean.Clean
import com.example.baozi_list.bean.Disinfection
import com.example.baozi_list.util.getDate
import com.example.baozi_list.util.getPreviousDay
import com.example.baozi_list.util.getTime
import com.example.baozi_list.util.isEarlyMorning
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private val dao = APP.getApp().database.getCarDao()
    private val disinfectionDao = APP.getApp().database.getDisinfectionDao()
    private val cleanDao = APP.getApp().database.getCleanDao()

    private val _carsLiveData: MutableLiveData<ArrayList<Car>> = MutableLiveData()
    val carsLiveData: LiveData<ArrayList<Car>> get() = _carsLiveData

    private val _singleCarLiveData: MutableLiveData<Int> = MutableLiveData()
    val singleCarLiveData: LiveData<Int> get() = _singleCarLiveData

    private val _disinfectionLveData: MutableLiveData<List<Disinfection>> = MutableLiveData()
    val disinfectionLveData: LiveData<List<Disinfection>> get() = _disinfectionLveData



    fun insertCar(car: Car) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertCar(car)
        }

    }

    fun insertDisinfection(disinfection: Disinfection) {
        viewModelScope.launch(Dispatchers.IO) {
            disinfectionDao.insertDisinfection(disinfection)
        }
    }



    fun insertCars(cars: List<Car>) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertCars(cars)
        }
    }

    fun insertDisinfections(disinfectionDataList: ArrayList<Disinfection>) {
        viewModelScope.launch(Dispatchers.IO) {
            disinfectionDao.insertDisinfections(disinfectionDataList)
        }
    }


    private suspend fun getCars(date: String): ArrayList<Car> {
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

    private suspend fun getDisinfections(date: String): ArrayList<Disinfection> {
        return withContext(Dispatchers.IO) {
            val list = ArrayList<Disinfection>()
            disinfectionDao.getTodayDisinfections(date)
                .flowOn(Dispatchers.IO)
                .map {
                    list.addAll(it)
                    list
                }.first()
        }
    }


    fun getInitCars() {
        viewModelScope.launch(Dispatchers.IO) {
            val time = getDate()

            when (APP.listType){
                APP.CAR->{
                    val list = getCars(time)
                    if (isEarlyMorning(getTime())) {
                        val previousDay = getPreviousDay()
                        list.addAll(getCars(previousDay))
                    }
                    _carsLiveData.postValue(list)
                }
                APP.DI->{
                    val list = getDisinfections(time)
                    if (isEarlyMorning(getTime())) {
                        val previousDay = getPreviousDay()
                        list.addAll(getDisinfections(previousDay))
                    }
                    _disinfectionLveData.postValue(list)
                }
                APP.CLEAN->{

                }
            }
        }
    }

    fun clearTodayData(cars: List<Car>) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.clearTodayData(cars)
        }
    }

    fun clearTodayDisinfection(disinfection: List<Disinfection>) {
        viewModelScope.launch(Dispatchers.IO) {
            disinfectionDao.clearTodayData(disinfection)
        }
    }

    fun clearTodayClean(clean:List<Clean>) {
        viewModelScope.launch(Dispatchers.IO) {
            cleanDao.clearTodayData(clean)
        }
    }



    fun updateCar(car: Car) {
        viewModelScope.launch(Dispatchers.IO) {
            car.foldDetail=false
            dao.updateCar(car)
        }
    }

    fun updateDisinfection(disinfection: Disinfection) {
        viewModelScope.launch(Dispatchers.IO) {
            disinfectionDao.updateDisinfection(disinfection)
        }
    }


    fun startUpdate() {
        _singleCarLiveData.postValue(1)
    }

    fun deleteCar(car: Car) {
        viewModelScope.launch (Dispatchers.IO){
            dao.deleteCar(car.trainNumber,car.backTrain,car.inTime,car.outTime,car.pickUpLane,car.addTime)
        }
    }




}