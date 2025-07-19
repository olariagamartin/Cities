package com.themarto.cities

import com.themarto.core.domain.City
import retrofit2.http.GET
import retrofit2.http.Url

interface CityNetworkApi {

    companion object {
        private const val CITIES_URL = "https://gist.githubusercontent.com/hernan-uala/dce8843a8edbe0b0018b32e137bc2b3a/raw/0996accf70cb0ca0e16f9a99e0ee185fafca7af1/cities.json"
    }
    @GET
    suspend fun getCities(@Url rawUrl: String = CITIES_URL): List<City>

}