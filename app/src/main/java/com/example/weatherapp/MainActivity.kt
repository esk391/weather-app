package com.example.weatherapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var editTextCity: EditText
    lateinit var buttonSearch: Button
    lateinit var textViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextCity = findViewById(R.id.editTextCity)
        buttonSearch = findViewById(R.id.buttonSearch)
        textViewResult = findViewById(R.id.textViewResult)

        buttonSearch.setOnClickListener {
            val city = editTextCity.text.toString().trim()
            if (city.isNotEmpty()) {
                getWeather(city)
            } else {
                Toast.makeText(this, "Enter a city name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getWeather(city: String) {
        val call = RetrofitClient.instance.getWeather(city)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    textViewResult.text = """
                        Location: ${data?.location}
                        Temperature: ${String.format("%.1f", data?.temperature)}°C
                        Condition: ${data?.description}
                        Humidity: ${data?.humidity}%
                        Wind Speed: ${String.format("%.1f", data?.windSpeed)} m/s
                    """.trimIndent()
                } else {
                    textViewResult.text = "Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                textViewResult.text = "Failed: ${t.message}"
            }
        })
    }
}
