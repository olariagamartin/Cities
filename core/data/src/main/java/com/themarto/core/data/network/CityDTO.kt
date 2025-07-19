package com.themarto.core.data.network

import com.squareup.moshi.Json

data class CityDTO(
    @Json(name = "_id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "country")
    val country: String,
    @Json(name = "coord")
    val coordinates: CoordinatesDTO
)
