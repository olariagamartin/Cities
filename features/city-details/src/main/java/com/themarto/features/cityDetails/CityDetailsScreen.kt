package com.themarto.features.cityDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.themarto.core.domain.City
import com.themarto.core.domain.Coordinates
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CityDetailsScreen(
    onNavigateBack: () -> Unit = { },
    cityId: String,
    viewModel: CityDetailViewModel = koinViewModel { parametersOf(cityId) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CityDetailsScreenContent(
        onBackClick = onNavigateBack,
        onFavoriteClick = viewModel::onFavoriteClick,
        uiState = uiState
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CityDetailsScreenContent(
    onBackClick: () -> Unit = { },
    onFavoriteClick: () -> Unit = { },
    uiState: UiState,
) {
    Column {
        DetailsTopAppBar(
            onBackClick = onBackClick,
            onFavoriteClick = onFavoriteClick,
            uiState = uiState
        )
        if (uiState.city != null) {
            DetailsContent(
                city =  uiState.city
            )
        }
    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DetailsTopAppBar(
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    uiState: UiState,
) {
    CenterAlignedTopAppBar(
        modifier = Modifier,
        title = {
            Text(
                text = "Details"
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (uiState.city?.isFavorite == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun DetailsContent(
    city: City
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier.size(100.dp),
                model = "https://flagsapi.com/${city.country}/flat/64.png",
                contentDescription = "Recipe image",
                placeholder = ColorPainter(Color.Gray),
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "${city.name}, ${city.country}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = "Coordinates:")
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "lat: ${city.coordinates.latitude}, lon: ${city.coordinates.longitude}")
        }
    }
}

@Preview
@Composable
private fun CityDetailsScreenPreview() {
    CityDetailsScreenContent(
        uiState = UiState(
            city = City(
                id = "123",
                name = "Cordoba",
                country = "AR",
                coordinates = Coordinates(1.0, 2.0),
                isFavorite = true
            )
        )
    )
}