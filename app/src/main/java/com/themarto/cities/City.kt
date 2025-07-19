package com.themarto.cities

import com.squareup.moshi.Json

data class City(
    @Json(name = "_id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "country")
    val country: String,
    @Json(name = "coord")
    val coordinates: Coordinates,
)
