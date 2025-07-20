package com.themarto.core.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class DBCity(
    @PrimaryKey val id: Int,
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val favorite: Boolean
)
