package com.themarto.features.cityDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Text("City Data")
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
                name = "Name",
                country = "CT",
                coordinates = Coordinates(1.0, 2.0),
                isFavorite = true
            )
        )
    )
}