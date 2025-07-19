package com.themarto.core.data.repository

import com.themarto.core.data.utils.Result
import com.themarto.core.domain.City

interface CityRepository {

    suspend fun getCities(): Result<List<City>>
}