package com.themarto.cities

import com.themarto.core.domain.City

interface CityRepository {

    suspend fun getCities(): Result<List<City>>
}