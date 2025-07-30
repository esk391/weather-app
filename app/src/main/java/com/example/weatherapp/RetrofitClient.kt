package com.example.weatherapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // For emulator: use 10.0.2.2:3000
    // For physical device: use your computer's IP address in the same network
    // Current IP: 192.168.1.103 - make sure this matches your computer's actual IP
    private const val BASE_URL = "http://192.168.1.103:3000"
    
    // Alternative URLs for different scenarios:
    // Emulator: "http://10.0.2.2:3000"
    // Physical device on same WiFi: "http://[YOUR_COMPUTER_IP]:3000"
    // Localhost (only works in emulator): "http://localhost:3000"

    val instance: WeatherApi by lazy {
        // Add logging for debugging network requests
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(WeatherApi::class.java)
    }
}
