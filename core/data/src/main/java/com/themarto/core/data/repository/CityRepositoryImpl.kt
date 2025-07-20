package com.themarto.core.data.repository

import android.util.Log
import com.themarto.core.data.database.CityDao
import com.themarto.core.data.network.CityNetworkApi
import com.themarto.core.data.utils.Result
import com.themarto.core.domain.City

class CityRepositoryImpl(
    private val cityApi: CityNetworkApi,
    private val cityDao: CityDao,
) : CityRepository {

    override suspend fun getCitiesFiltered(prefix: String): Result<List<City>> {
        val dbResult = cityDao.getByName(prefix)
        if (dbResult.isEmpty() && prefix.isEmpty()) {
            Log.d("CityRepository", "DB is empty, fetching from API")
            val apiResult = cityApi.getCities()
            cityDao.insertAll(apiResult.map { it.toDB() })
            return Result.Success(cityDao.getAll().map { it.toDomain() })
        }
        Log.d("CityRepository", "DB is not empty")
        return Result.Success(dbResult.map { it.toDomain() })
    }

    override suspend fun toggleFavorite(id: String) {
        cityDao.toggleFavourite(id)
    }
}