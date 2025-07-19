package com.themarto.cities

import com.themarto.core.domain.City

class CityRepositoryImpl(
    private val cityApi: CityNetworkApi
) : CityRepository {
    override suspend fun getCities(): Result<List<City>> {
        return Result.Success(cityApi.getCities())
    }
}