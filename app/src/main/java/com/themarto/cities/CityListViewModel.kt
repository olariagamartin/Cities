package com.themarto.cities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CityListUIState(
    val cities: List<City> = emptyList()
)

class CityListViewModel(
    private val cityRepository: CityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CityListUIState())
    val uiState: StateFlow<CityListUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = CityListUIState(cityRepository.getCities())
        }
    }

}