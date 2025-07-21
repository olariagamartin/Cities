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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
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
    coordinates: Coordinates? = null
) {
    if (coordinates == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Select a City",
            )
        }
    } else {
        val cameraPositionState = rememberCameraPositionState()

        val markerState = remember(coordinates) {
            MarkerState(position = LatLng(coordinates.latitude, coordinates.longitude))
        }

        LaunchedEffect(coordinates) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(
                        LatLng(coordinates.latitude, coordinates.longitude),
                        10f
                    ),
                ),
                durationMs = 1000
            )

        }

        GoogleMap(
            modifier = modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false),
            properties = MapProperties(isMyLocationEnabled = false)
        ) {
            Marker(
                state = markerState,
                title = "Selected city"
            )
        }
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