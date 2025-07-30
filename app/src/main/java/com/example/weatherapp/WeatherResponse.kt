package com.example.weatherapp

data class WeatherResponse(
    val location: String,
    val temperature: Double,
    val description: String,
    val humidity: Int,
    val windSpeed: Double
)
