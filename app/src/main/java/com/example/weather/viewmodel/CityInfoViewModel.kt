package com.example.weather.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.WeatherModel
import com.example.weather.network.WeatherApi
import kotlinx.coroutines.launch

enum class WeatherApiStatus { LOADING, ERROR, DONE }

class CityInfoViewModel : ViewModel() {
    private val _cityName = MutableLiveData<String>()
    val cityName: LiveData<String> = _cityName

    private val _weatherData = MutableLiveData<WeatherModel>()
    val weatherData: LiveData<WeatherModel> = _weatherData

    private val _status = MutableLiveData<WeatherApiStatus>()
    val status: LiveData<WeatherApiStatus> = _status


    fun initWeatherData(desiredCityName: String) {
        viewModelScope.launch {
            _status.value = WeatherApiStatus.LOADING
            try {
                _weatherData.value = WeatherApi.retrofitService.getData(desiredCityName) //burası çalışması lazım ama asla emin değilim
                _status.value = WeatherApiStatus.DONE
                _cityName.value?.let { Log.d("initWeatherData", it) }
            } catch (e: Exception) {
                _status.value = WeatherApiStatus.ERROR
                _weatherData.value = null
                Log.d("initWeatherDataE",e.toString())
            }
        }
    }


}