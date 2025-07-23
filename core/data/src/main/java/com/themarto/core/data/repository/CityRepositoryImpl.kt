package com.themarto.core.data.repository

import android.util.Log
import com.themarto.core.data.database.CityDao
import com.themarto.core.data.network.CityNetworkApi
import com.themarto.core.data.utils.Result
import com.themarto.core.domain.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CityRepositoryImpl(
    private val cityApi: CityNetworkApi,
    private val cityDao: CityDao,
) : CityRepository {

    override suspend fun getCitiesFiltered(prefix: String, filterFav: Boolean): Flow<Result<List<City>>> {
        if (cityDao.hasAny() == null) {
            Log.d("CityRepository", "DB is empty, fetching from API")
            val apiResult = cityApi.getCities()
            cityDao.insertAll(apiResult.map { it.toDB() })

        }
        return cityDao.getFiltered(prefix, filterFav)
            .map { cityListDb ->
                Result.Success(
                    cityListDb.map { it.toDomain() }
                )
            }
    }

    override suspend fun toggleFavorite(id: String) {
        cityDao.toggleFavourite(id)
    }

    override suspend fun getCityById(id: String): Flow<Result<City>> {
        return cityDao.getById(id).map { Result.Success(it.toDomain()) }
    }

}