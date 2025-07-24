package com.themarto.features.cityList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.themarto.core.data.repository.CityRepository
import com.themarto.core.data.utils.Result
import com.themarto.core.data.utils.isSuccess
import com.themarto.core.domain.City
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CityListUIState(
    val cities: Flow<PagingData<City>>? = null,
    val error: String? = null,
    val query: String = "",
    val filterFav: Boolean = false,
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

    private val citiesFlow: Flow<PagingData<City>> = cityListResponse.flatMapLatest { result ->
        if (result.isSuccess()) {
            flow<PagingData<City>> {
                emit(result.data)
            }
        } else {
            _uiState.update {
                it.copy(
                    error = (result as Result.Error).error
                )
            }
            flow<PagingData<City>> {
                emit(PagingData.empty())
            }
        }
    }.cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(CityListUIState())
    val uiState: StateFlow<CityListUIState> = _uiState.asStateFlow()

    init {
        loadCities()
    }

    private fun loadCities() {
        _uiState.update {
            it.copy(
                cities = citiesFlow
            )
        }
    }

    fun onQueryChanged(searchPrefix: String) {
        _uiState.update {
            it.copy(
                query = searchPrefix,
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
            )
        }
        filterFav.value = !(filterFav.value)
    }

}