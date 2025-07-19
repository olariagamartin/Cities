package com.themarto.core.data.repository

import com.themarto.core.data.network.CityNetworkApi
import com.themarto.core.data.utils.Result
import com.themarto.core.domain.City

class CityRepositoryImpl(
    private val cityApi: CityNetworkApi
) : CityRepository {
    override suspend fun getCities(): Result<List<City>> {
        return Result.Success(cityApi.getCities())
    }
}