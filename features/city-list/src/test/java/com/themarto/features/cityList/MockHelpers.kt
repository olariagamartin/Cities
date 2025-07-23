package com.themarto.features.cityList

import com.themarto.core.data.repository.CityRepository
import com.themarto.core.data.utils.Result
import com.themarto.core.domain.City
import com.themarto.core.domain.Coordinates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// duplicated: could be in a separate test module
fun provideCityRepository(
    getCitiesFiltered: Flow<Result<List<City>>> = flow {
        emit(Result.Success(provideCityList()))
    }
): CityRepository {
    return object : CityRepository {

        override suspend fun getCitiesFiltered(prefix: String, filterFav: Boolean): Flow<Result<List<City>>> {
            return getCitiesFiltered
        }

        override suspend fun toggleFavorite(id: String) {
            // nothing
        }

        override suspend fun getCityById(id: String): Flow<Result<City>> = flow {
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