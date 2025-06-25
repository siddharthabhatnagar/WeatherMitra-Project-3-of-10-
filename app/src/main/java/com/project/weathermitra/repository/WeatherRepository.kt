package com.project.weathermitra.repository

import android.util.Log
import com.project.weathermitra.models.WeatherResponse
import com.project.weathermitra.network.WeatherApiService
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WeatherApiService) {
    suspend fun fetchCurrent(city: String, apiKey: String): Result<WeatherResponse> {
        return try {
            val resp = api.getCurrentWeather(apiKey, city)
            if (resp.isSuccessful) {
                Log.d("TAG", "fetchCurrent: resp successfull")
                resp.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response body"))
            } else {
                Log.d("TAG", "fetchCurrent: resp fail")
                Result.failure(Exception("HTTP ${resp.code()}: ${resp.message()}"))
            }
        } catch (t: Throwable) {
            Log.d("TAG", "fetchCurrent: ${t.message}")
            Result.failure(t)
        }
    }
}

