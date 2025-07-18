package com.themarto.cities

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CityListUIState(
    val cities: List<City> = emptyList()
)

class CityListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CityListUIState())
    val uiState: StateFlow<CityListUIState> = _uiState.asStateFlow()

}