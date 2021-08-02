package com.example.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.WeatherModel
import com.example.weather.network.WeatherApi
import kotlinx.coroutines.launch

enum class WeatherApiStatus { LOADING, ERROR, DONE, STANDBY }

class CityInfoViewModel : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherModel>()
    val weatherData: LiveData<WeatherModel> = _weatherData

    private val _status = MutableLiveData<WeatherApiStatus>()
    val status: LiveData<WeatherApiStatus> = _status

    fun setStatus(x : WeatherApiStatus){
        _status.value = x
    }

    fun initWeatherData(desiredCityName: String){
        //1
        viewModelScope.launch {
            //3
            _status.value = WeatherApiStatus.LOADING
            try {
                //4
                _weatherData.value = WeatherApi.retrofitService.getData(desiredCityName)
                _status.value = WeatherApiStatus.DONE
            } catch (e: Exception) {
                //5
                _weatherData.value = null
                _status.value = WeatherApiStatus.ERROR
            }
        }
        //2
        _status.value = WeatherApiStatus.STANDBY
    }
}