package com.themarto.features.cityList

import androidx.paging.PagingData
import com.themarto.core.domain.City
import com.themarto.core.domain.Coordinates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

private val citiesFakeData = listOf(
    City("id", "Name", "CT", Coordinates(1.0, 2.0), true),
    City("id", "Name", "CT", Coordinates(1.0, 2.0), false),
    City("id", "Name", "CT", Coordinates(1.0, 2.0), true),
    City("id", "Name", "CT", Coordinates(1.0, 2.0), false),
)

fun previewPagingFlow(): Flow<PagingData<City>> =
    flowOf(PagingData.from(citiesFakeData))
