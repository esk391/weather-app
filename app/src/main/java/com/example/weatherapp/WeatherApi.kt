package com.example.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherApi {
    @GET("/getInformation/{city}")
    fun getWeather(@Path("city") city: String): Call<WeatherResponse>
}
