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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CityListUIState(
    val cities: List<City> = emptyList(),
    val error: String? = null,
    val query: String = ""
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
                _uiState.update { it.copy(cities = result.data) }
            } else if (result.isError()) {
                _uiState.update { it.copy(error = result.error) }
            }
        }
    }

    fun onQueryChanged(searchPrefix: String) {
        _uiState.update { it.copy(query = searchPrefix) }
        _uiState.value = _uiState.value.copy(query = searchPrefix)
        viewModelScope.launch {
            val result = cityRepository.getCitiesFiltered(searchPrefix)
            if (result.isSuccess()) {
                _uiState.update { it.copy(cities = result.data) }
            }
            else if (result.isError()) {
                _uiState.update { it.copy(error = result.error) }
            }
        }
    }

}