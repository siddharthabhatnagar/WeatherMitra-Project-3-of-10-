package com.project.weathermitra.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.weathermitra.models.WeatherResponse
import com.project.weathermitra.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repo: WeatherRepository): ViewModel() {
    private val _state = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val state: StateFlow<WeatherState> = _state

    fun loadWeather(city: String, apiKey: String) = viewModelScope.launch {
        _state.value = WeatherState.Loading
        repo.fetchCurrent(city, apiKey).fold(
            onSuccess = { _state.value = WeatherState.Success(it) },
            onFailure = { _state.value = WeatherState.Error(it.localizedMessage ?: "Unknown") }
        )
    }
}

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val data: WeatherResponse) : WeatherState()
    data class Error(val message: String) : WeatherState()
}

