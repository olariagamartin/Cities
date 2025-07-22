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

    override suspend fun getCitiesFiltered(prefix: String, filterFav: Boolean): Result<List<City>> {
        if (cityDao.hasAny() == null) {
            Log.d("CityRepository", "DB is empty, fetching from API")
            val apiResult = cityApi.getCities()
            cityDao.insertAll(apiResult.map { it.toDB() })
            return Result.Success(
                cityDao.getFiltered(prefix, filterFav)
                    .map { it.toDomain() }
            )
        }
        Log.d("CityRepository", "DB is not empty")
        return Result.Success(
            cityDao.getFiltered(prefix, filterFav)
                .map { it.toDomain() }
        )
    }

    override suspend fun toggleFavorite(id: String) {
        cityDao.toggleFavourite(id)
    }

    override suspend fun getCityById(id: String): Result<City> {
        return Result.Success(cityDao.getById(id).toDomain())
    }

}