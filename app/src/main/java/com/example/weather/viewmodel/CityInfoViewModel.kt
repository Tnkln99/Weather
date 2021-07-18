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
    private val _icon = MutableLiveData<Int>()
    val icon: LiveData<Int> = _icon

    private val _weatherData = MutableLiveData<WeatherModel>()
    val weatherData: LiveData<WeatherModel> = _weatherData

    private val _status = MutableLiveData<WeatherApiStatus>()
    val status: LiveData<WeatherApiStatus> = _status

    /*fun setImage(weatherDesc: String) : Int {
        when(weatherDesc){
            "clear sky" -> _image.value = R.drawable._1d
            "few clouds" -> _image.value = R.drawable._2d
            "scattered clouds" -> _image.value = R.drawable._3d
            "broken clouds" -> _image.value = R.drawable._4d
            "shower rain" -> _image.value = R.drawable._0d
            "rain" -> _image.value = R.drawable._9d
            else -> _image.value = R.drawable.unknown
        }
        return _image.value!!
    }*/


    fun initWeatherData(desiredCityName: String) {
        viewModelScope.launch {
            _status.value = WeatherApiStatus.LOADING
            try {
                _weatherData.value = WeatherApi.retrofitService.getData(desiredCityName)
                _status.value = WeatherApiStatus.DONE
            } catch (e: Exception) {
                _status.value = WeatherApiStatus.ERROR
                _weatherData.value = null
                Log.d("initWeatherDataE",e.toString())
            }
        }
    }


}