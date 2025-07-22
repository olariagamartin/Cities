package com.themarto.features.cityDetails

import androidx.lifecycle.ViewModel
import com.themarto.core.domain.City
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UiState(
    val city: City? = null
)

class CityDetailViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
}