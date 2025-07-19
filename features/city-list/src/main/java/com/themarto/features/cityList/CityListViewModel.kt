package com.themarto.features.cityList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.themarto.core.data.repository.CityRepository
import com.themarto.core.data.utils.isError
import com.themarto.core.data.utils.isSuccess
import com.themarto.core.domain.City
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CityListUIState(
    val cities: List<City> = emptyList(),
    val error: String? = null
)

class CityListViewModel(
    private val cityRepository: CityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CityListUIState())
    val uiState: StateFlow<CityListUIState> = _uiState.asStateFlow()

    init {
        loadCities()
    }

    private fun loadCities() {
        viewModelScope.launch {
            val result = cityRepository.getCities()
            if (result.isSuccess()) {
                _uiState.value = CityListUIState(cities = result.data)
            } else if (result.isError()) {
                _uiState.value = CityListUIState(error = result.error)
            }
        }
    }

    fun filterByPrefix(searchPrefix: String) {
        viewModelScope.launch {
            val result = cityRepository.getCitiesFiltered(searchPrefix)
            if (result.isSuccess()) {
                _uiState.value = CityListUIState(cities = result.data)
            }
            else if (result.isError()) {
                _uiState.value = CityListUIState(error = result.error)
            }
        }
    }

}