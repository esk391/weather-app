package com.example.weatherapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    lateinit var editTextCity: EditText
    lateinit var buttonSearch: Button
    lateinit var textViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextCity = findViewById(R.id.editTextCity)
        buttonSearch = findViewById(R.id.buttonSearch)
        textViewResult = findViewById(R.id.textViewResult)

        // Display connection info on startup
        textViewResult.text = "Ready to search weather!\n\nServer: http://192.168.1.103:3000\nAPI endpoint: /getInformation/{city}"

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
        textViewResult.text = "Searching for weather in $city...\nConnecting to server..."
        Log.d(TAG, "Making request for city: $city")
        
        val call = RetrofitClient.instance.getWeather(city)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                Log.d(TAG, "Response received: ${response.code()}")
                if (response.isSuccessful) {
                    val data = response.body()
                    Log.d(TAG, "Response data: $data")
                    textViewResult.text = """
                        Location: ${data?.location}
                        Temperature: ${String.format("%.1f", data?.temperature)}°C
                        Condition: ${data?.description}
                        Humidity: ${data?.humidity}%
                        Wind Speed: ${String.format("%.1f", data?.windSpeed)} m/s
                    """.trimIndent()
                } else {
                    val errorMsg = "Server Error: HTTP ${response.code()}\n${response.message()}"
                    Log.e(TAG, errorMsg)
                    textViewResult.text = errorMsg
                    Toast.makeText(this@MainActivity, "Server returned error: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                val errorMsg = "Connection Failed: ${t.message}"
                Log.e(TAG, errorMsg, t)
                textViewResult.text = """
                    Connection Failed!
                    
                    Error: ${t.message}
                    
                    Possible causes:
                    • Express server not running on port 3000
                    • Wrong IP address (192.168.1.103)
                    • Phone and computer not on same WiFi
                    • Firewall blocking connections
                    
                    Try:
                    1. Check if server is running: http://192.168.1.103:3000
                    2. Verify your computer's IP address
                    3. Make sure phone and computer are on same network
                """.trimIndent()
                Toast.makeText(this@MainActivity, "Cannot connect to server", Toast.LENGTH_LONG).show()
            }
        })
    }
}
