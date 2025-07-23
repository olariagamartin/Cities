package com.themarto.features.cityDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.themarto.core.data.repository.CityRepository
import com.themarto.core.data.utils.Result
import com.themarto.core.data.utils.isSuccess
import com.themarto.core.domain.City
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val city: City? = null
)

class CityDetailViewModel(
    private val cityRepository: CityRepository,
    private val cityId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadCity()
    }

    private fun loadCity() {
        viewModelScope.launch {
            cityRepository.getCityById(cityId).collectLatest { result ->
                if (result.isSuccess()) {
                    _uiState.update {
                        it.copy(city = result.data)
                    }
                }
            }
        }
    }

    fun onFavoriteClick() {
        viewModelScope.launch {
            cityRepository.toggleFavorite(cityId)
            loadCity()
        }
    }
}