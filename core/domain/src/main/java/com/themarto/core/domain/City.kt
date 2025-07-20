package com.themarto.core.domain

data class City(
    val id: String,
    val name: String,
    val country: String,
    val coordinates: Coordinates,
    val isFavorite: Boolean
)
