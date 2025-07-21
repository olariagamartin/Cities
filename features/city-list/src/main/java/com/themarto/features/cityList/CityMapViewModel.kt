package com.themarto.features.cityList

import androidx.lifecycle.ViewModel
import com.themarto.core.domain.City
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CityMapUiState(
    val city: City? = null,
)

class CityMapViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CityMapUiState())
    val uiState: StateFlow<CityMapUiState> = _uiState.asStateFlow()
}