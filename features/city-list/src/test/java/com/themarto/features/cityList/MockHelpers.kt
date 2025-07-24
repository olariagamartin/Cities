package com.themarto.features.cityList

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.themarto.core.data.repository.CityRepository
import com.themarto.core.data.utils.Result
import com.themarto.core.domain.City
import com.themarto.core.domain.Coordinates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

// duplicated: could be in a separate test module
fun provideCityRepository(
    getCitiesFiltered: Flow<Result<PagingData<City>>> = flowOf (
        Result.Success(
            PagingData.from(
                data = provideCityList(),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(true),
                    prepend = LoadState.NotLoading(true),
                    append = LoadState.NotLoading(true)
                )
            )
        )
    )
): CityRepository {
    return object : CityRepository {

        override fun getCitiesFiltered(prefix: String, filterFav: Boolean): Flow<Result<PagingData<City>>> {
            return getCitiesFiltered
        }

        override suspend fun toggleFavorite(id: String) {
            // nothing
        }

        override fun getCityById(id: String): Flow<Result<City>> = flow {
            emit(Result.Success(provideCityList().first { it.id == id }))
        }
    }
}

fun provideCityList() : List<City> {
    return listOf(
        City("123", "name", "country", Coordinates(1.0, 2.0), false),
        City("124", "name", "country", Coordinates(1.0, 2.0), false),
        City("125", "name", "country", Coordinates(1.0, 2.0), false),
    )
}