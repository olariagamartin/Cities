package com.themarto.cities

class CityRepositoryImpl(
    private val cityApi: CityNetworkApi
) : CityRepository {
    override suspend fun getCities(): Result<List<City>> {
        return Result.Success(cityApi.getCities())
    }
}