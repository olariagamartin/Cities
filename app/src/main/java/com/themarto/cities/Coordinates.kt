package com.themarto.cities

import com.squareup.moshi.Json

data class Coordinates(
    @Json(name = "lon")
    val longitude: Double,
    @Json(name = "lat")
    val latitude: Double,
)
