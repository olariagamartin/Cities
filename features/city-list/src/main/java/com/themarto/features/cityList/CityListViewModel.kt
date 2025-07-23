package com.themarto.features.cityList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.themarto.core.data.repository.CityRepository
import com.themarto.core.data.utils.isError
import com.themarto.core.data.utils.isSuccess
import com.themarto.core.domain.City
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CityListUIState(
    val cities: List<City> = emptyList(),
    val error: String? = null,
    val query: String = "",
    val filterFav: Boolean = false,
    val loading: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class CityListViewModel(
    private val cityRepository: CityRepository
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val filterFav = MutableStateFlow(false)

    private val cityListResponse = combine(query, filterFav) { query, filterFav ->
        query to filterFav
    }.flatMapLatest { (query, filterFav) ->
        cityRepository.getCitiesFiltered(query, filterFav)
    }

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
        viewModelScope.launch {
            cityListResponse.collect { result ->
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
    }

    fun onQueryChanged(searchPrefix: String) {
        _uiState.update {
            it.copy(
                query = searchPrefix,
                loading = true
            )
        }
        query.value = searchPrefix
    }



    fun onFavClick(id: String) {
        viewModelScope.launch {
            cityRepository.toggleFavorite(id)
        }
    }

    fun onFilterFavClick() {
        _uiState.update {
            it.copy(
                filterFav = !it.filterFav,
                loading = true
            )
        }
        filterFav.value = !(filterFav.value)
    }

}