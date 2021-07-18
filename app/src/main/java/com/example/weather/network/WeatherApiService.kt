package com.example.weather.network
import com.example.weather.model.WeatherModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL =
    "https://api.openweathermap.org"

private const val APPID =
    "19c083cc98e375372cd1662d02094eb9"


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val httpClient =  OkHttpClient.Builder().apply {
        this.addInterceptor(logging)
    }

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(httpClient.build())
    .build()

interface WeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getData(
        @Query("q") cityName: String,
        @Query("APPID") appid : String = APPID
    ): WeatherModel
}

object WeatherApi {
    val retrofitService: WeatherApiService by lazy { retrofit.create(WeatherApiService::class.java) }
}