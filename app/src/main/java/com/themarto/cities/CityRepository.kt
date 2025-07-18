package com.themarto.cities

interface CityRepository {

    suspend fun getCities(): List<City>
}