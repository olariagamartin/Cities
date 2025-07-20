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
    val query: String = "",
    val filterFav: Boolean = false,
    val loading: Boolean = false
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
        _uiState.update {
            it.copy(
                loading = true
            )
        }
        refreshCityList()
    }

    private fun refreshCityList(
        searchPrefix: String = _uiState.value.query,
        filterFav: Boolean = _uiState.value.filterFav
    ) {
        viewModelScope.launch {
            val result = cityRepository.getCitiesFiltered(searchPrefix, filterFav)
            if (result.isSuccess()) {
                _uiState.update {
                    it.copy(
                        cities = result.data,
                        loading = false
                    )
                }
            } else if (result.isError()) {
                _uiState.update {
                    it.copy(
                        error = result.error,
                        loading = false
                    )
                }
            }
        }
    }

    fun onQueryChanged(searchPrefix: String) {
        _uiState.update {
            it.copy(
                query = searchPrefix,
                loading = true
            )
        }
        refreshCityList(searchPrefix)
    }



    fun onFavClick(id: String) {
        viewModelScope.launch {
            cityRepository.toggleFavorite(id)
            refreshCityList(_uiState.value.query)
        }
    }

    fun onFilterFavClick() {
        _uiState.update {
            it.copy(
                filterFav = !it.filterFav,
                loading = true
            )
        }
        refreshCityList(
            _uiState.value.query,
            _uiState.value.filterFav
        )
    }

}