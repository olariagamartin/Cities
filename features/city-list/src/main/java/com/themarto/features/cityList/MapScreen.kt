package com.themarto.features.cityList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.themarto.core.domain.City
import com.themarto.core.domain.Coordinates
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MapScreen(
    onNavigateBack: () -> Unit = { },
    cityId: String,
    viewModel: CityMapViewModel = koinViewModel { (parametersOf(cityId)) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.city != null) {
        MapScreenContent(
            uiState = uiState,
            onNavigateBack = onNavigateBack
        )
    }

}

@Composable
fun MapScreenContent(
    uiState: CityMapUiState,
    onNavigateBack: () -> Unit = { },
) {
    Column {
        MapTopAppBar(
            city = uiState.city!!,
            onNavigateBack = onNavigateBack
        )
        MapContainer(
            coordinates = uiState.city.coordinates
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapTopAppBar(
    city: City,
    onNavigateBack: () -> Unit = { },
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = "${city.name}, ${city.country}"
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
fun MapContainer(
    modifier: Modifier = Modifier,
    coordinates: Coordinates?,
) {
    // TODO
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Blue)
    ) {
        Text(
            text = "Coordinates: ${coordinates?.latitude}, ${coordinates?.longitude}",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview
@Composable
private fun MapScreenPreview() {
    MapScreenContent(
        uiState = CityMapUiState(
            city = City(
                id = "123",
                name = "Name",
                country = "CT",
                coordinates = Coordinates(1.0, 2.0),
                isFavorite = true
            )
        )
    )
}