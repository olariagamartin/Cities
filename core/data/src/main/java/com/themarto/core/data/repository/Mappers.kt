package com.themarto.core.data.repository

import com.themarto.core.data.network.CityDTO
import com.themarto.core.data.network.CoordinatesDTO
import com.themarto.core.domain.City
import com.themarto.core.domain.Coordinates

fun CityDTO.toDomain() = City(
    id = this.id,
    name = this.name,
    country = this.country,
    coordinates = this.coordinates.toDomain()
)

fun CoordinatesDTO.toDomain() = Coordinates(
    longitude = this.longitude,
    latitude = this.latitude
)