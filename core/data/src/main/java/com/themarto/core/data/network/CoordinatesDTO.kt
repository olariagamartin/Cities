package com.themarto.core.data.network

import com.squareup.moshi.Json

data class CoordinatesDTO(
    @Json(name = "lon")
    val longitude: Double,
    @Json(name = "lat")
    val latitude: Double,
)
