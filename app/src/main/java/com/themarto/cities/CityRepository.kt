package com.themarto.cities

interface CityRepository {

    suspend fun getCities(): Result<List<City>>
}