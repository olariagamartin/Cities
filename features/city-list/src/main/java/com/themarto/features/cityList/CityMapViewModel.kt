package com.themarto.features.cityList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.themarto.core.data.repository.CityRepository
import com.themarto.core.data.utils.isSuccess
import com.themarto.core.domain.City
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CityMapUiState(
    val city: City? = null,
)

class CityMapViewModel(
    private val cityRepository: CityRepository,
    private val cityId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(CityMapUiState())
    val uiState: StateFlow<CityMapUiState> = _uiState.asStateFlow()

    init {
        loadCity()
    }

    private fun loadCity() {
        viewModelScope.launch {
            val result = cityRepository.getCityById(cityId)
            if (result.isSuccess()) {
                _uiState.update {
                    it.copy(
                        city = result.data
                    )
                }
            }

        }
    }
}