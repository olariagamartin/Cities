package com.themarto.features.cityList

import com.themarto.core.data.repository.CityRepository
import com.themarto.core.data.utils.Result
import com.themarto.core.domain.City
import com.themarto.core.domain.Coordinates

fun provideCityRepository(
    getCitiesFiltered: suspend (String, Boolean) -> Result<List<City>> = { prefix, filterFav ->
        Result.Success(provideCityList()
            .filter {
                it.name.startsWith(prefix).and(!filterFav || it.isFavorite)
            })
    }
): CityRepository {
    return object : CityRepository {

        override suspend fun getCitiesFiltered(prefix: String, filterFav: Boolean): Result<List<City>> {
            return getCitiesFiltered(prefix, filterFav)
        }

        override suspend fun toggleFavorite(id: String) {
            // nothing
        }

        override suspend fun getCityById(id: String): Result<City> {
            return Result.Success(provideCityList().first { it.id == id })
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