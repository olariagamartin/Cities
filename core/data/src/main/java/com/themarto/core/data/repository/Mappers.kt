package com.themarto.core.data.repository

import com.themarto.core.data.database.DBCity
import com.themarto.core.data.network.CityDTO
import com.themarto.core.data.network.CoordinatesDTO
import com.themarto.core.domain.City
import com.themarto.core.domain.Coordinates

fun CityDTO.toDomain() = City(
    id = this.id,
    name = this.name,
    country = this.country,
    coordinates = this.coordinates.toDomain(),
    isFavorite = false
)

fun CoordinatesDTO.toDomain() = Coordinates(
    longitude = this.longitude,
    latitude = this.latitude
)

fun DBCity.toDomain() = City(
    id = this.id.toString(),
    name = this.name,
    country = this.country,
    coordinates = Coordinates(
        longitude = this.longitude,
        latitude = this.latitude
    ),
    isFavorite = this.favorite
)

fun CityDTO.toDB() = DBCity(
    id = this.id.toInt(),
    name = this.name,
    country = this.country,
    latitude = this.coordinates.latitude,
    longitude = this.coordinates.longitude,
    favorite = false
)