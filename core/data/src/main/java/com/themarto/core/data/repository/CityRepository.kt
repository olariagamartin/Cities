package com.themarto.core.data.repository

import com.themarto.core.data.utils.Result
import com.themarto.core.domain.City

interface CityRepository {

    suspend fun getCitiesFiltered(prefix: String, filterFav: Boolean = false): Result<List<City>>

    suspend fun toggleFavorite(id: String)
}