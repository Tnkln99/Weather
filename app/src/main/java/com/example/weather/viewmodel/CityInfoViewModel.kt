package com.example.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.WeatherModel
import com.example.weather.network.WeatherApi
import kotlinx.coroutines.launch

enum class WeatherApiStatus { LOADING, ERROR, DONE }

class CityInfoViewModel : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherModel>()
    val weatherData: LiveData<WeatherModel> = _weatherData

    private val _status = MutableLiveData<WeatherApiStatus>()
    val status: LiveData<WeatherApiStatus> = _status



    fun initWeatherData(desiredCityName: String) : Boolean {
        viewModelScope.launch {
            _status.value = WeatherApiStatus.LOADING
            try {
                _status.value = WeatherApiStatus.DONE
                _weatherData.value = WeatherApi.retrofitService.getData(desiredCityName)
            } catch (e: Exception) {
                _status.value = WeatherApiStatus.ERROR
                //Log.d("inViewModelE",_status.value.toString())
                _weatherData.value = null
                _status.value = WeatherApiStatus.ERROR
            }
        }
        //Log.d("inViewModel",_status.value.toString())
        return _status.value == WeatherApiStatus.DONE
    }
}