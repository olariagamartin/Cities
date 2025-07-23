package com.themarto.core.data.repository

import com.themarto.core.data.utils.Result
import com.themarto.core.domain.City
import kotlinx.coroutines.flow.Flow

interface CityRepository {

    suspend fun getCitiesFiltered(prefix: String, filterFav: Boolean = false): Flow<Result<List<City>>>

    suspend fun toggleFavorite(id: String)

    suspend fun getCityById(id: String): Flow<Result<City>>
}