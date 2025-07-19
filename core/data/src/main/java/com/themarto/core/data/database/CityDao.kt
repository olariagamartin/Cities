package com.themarto.core.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CityDao {

    @Insert
    suspend fun insertAll(cities: List<DBCity>)

    @Query("SELECT * FROM cities ORDER BY name, country")
    suspend fun getAll(): List<DBCity>


}