package com.themarto.core.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CityDao {

    @Query("SELECT 1 FROM cities LIMIT 1")
    suspend fun hasAny(): Int?

    @Insert
    suspend fun insertAll(cities: List<DBCity>)

    @Query("""
        SELECT * FROM cities
        WHERE (name LIKE :namePrefix || '%' COLLATE NOCASE) 
            AND (:onlyFavs = 0 OR favorite = 1)
        ORDER BY name, country
    """
    )
    suspend fun getFiltered(namePrefix: String, onlyFavs: Boolean = false): List<DBCity>

    @Query("UPDATE cities SET favorite = CASE favorite WHEN 1 THEN 0 ELSE 1 END WHERE id = :id")
    suspend fun toggleFavourite(id: String)

    @Query("SELECT * FROM cities WHERE id = :id")
    suspend fun getById(id: String): DBCity

}