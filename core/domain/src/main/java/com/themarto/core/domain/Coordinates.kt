package com.themarto.core.domain

import com.squareup.moshi.Json

data class Coordinates(
    @Json(name = "lon")
    val longitude: Double,
    @Json(name = "lat")
    val latitude: Double,
)
