package com.themarto.core.data.repository

import android.util.Log
import com.themarto.core.data.database.CityDao
import com.themarto.core.data.network.CityNetworkApi
import com.themarto.core.data.utils.Result
import com.themarto.core.domain.City
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class CityRepositoryImpl(
    private val cityApi: CityNetworkApi,
    private val cityDao: CityDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CityRepository {

    override fun getCitiesFiltered(prefix: String, filterFav: Boolean): Flow<Result<List<City>>> =
        flow {
            populateDBIfEmpty()
            cityDao.getFiltered(prefix, filterFav)
                .map { cityListDb ->
                    Result.Success(
                        cityListDb.map { it.toDomain() }
                    )
                }.let { emitAll(it) }
        }.flowOn(ioDispatcher)

    private suspend fun populateDBIfEmpty() {
        if (cityDao.hasAny() == null) {
            Log.d("CityRepository", "DB is empty, fetching from API")
            val apiResult = cityApi.getCities()
            cityDao.insertAll(apiResult.map { it.toDB() })
        }
    }

    override suspend fun toggleFavorite(id: String) {
        cityDao.toggleFavourite(id)
    }

    override fun getCityById(id: String): Flow<Result<City>> {
        return cityDao.getById(id).map { Result.Success(it.toDomain()) }
    }

}