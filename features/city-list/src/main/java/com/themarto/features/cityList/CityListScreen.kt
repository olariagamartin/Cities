package com.themarto.features.cityList

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.themarto.core.domain.City
import com.themarto.core.domain.Coordinates
import org.koin.androidx.compose.koinViewModel

@Composable
fun CitiesScreen(
    viewModel: CityListViewModel = koinViewModel(),
    navigateToCityMap: (String) -> Unit = { },
    navigateToCityDetails: (String) -> Unit = { },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val config = LocalConfiguration.current
    val isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE

    var selectedId by rememberSaveable { mutableStateOf<String?>(null) }

    if (isLandscape) {
        CityListWithMap(
            uiState = uiState,
            onQueryChange = viewModel::onQueryChanged,
            onFavClick = viewModel::onFavClick,
            onFilterFavs = viewModel::onFilterFavClick,
            onCityClick = { cityId -> selectedId = cityId },
            onInfoClick = navigateToCityDetails,
            selectedId = selectedId
        )
    } else {
        CitiesScreenContent(
            uiState = uiState,
            onQueryChange = viewModel::onQueryChanged,
            onFavClick = viewModel::onFavClick,
            onFilterFavs = viewModel::onFilterFavClick,
            onCityClick = navigateToCityMap,
            onInfoClick = navigateToCityDetails,
        )
    }
}

@Composable
private fun CityListWithMap(
    uiState: CityListUIState,
    onQueryChange: (String) -> Unit = {},
    onFavClick: (String) -> Unit = { },
    onFilterFavs: () -> Unit = { },
    onCityClick: (String) -> Unit = { },
    onInfoClick: (String) -> Unit = { },
    selectedId: String?,
) {
    val selectedCity = remember(selectedId) {
        uiState.cities.find { it.id == selectedId }
    }
    Row {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            CitiesScreenContent(
                uiState = uiState,
                onQueryChange = onQueryChange,
                onFavClick = onFavClick,
                onFilterFavs = onFilterFavs,
                onCityClick = onCityClick,
                onInfoClick = onInfoClick,
                selectedId = selectedId
            )
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            MapContainer(
                coordinates = selectedCity?.coordinates
            )
        }
    }
}

@Composable
fun CitiesScreenContent(
    uiState: CityListUIState,
    onQueryChange: (String) -> Unit = {},
    onFavClick: (String) -> Unit = { },
    onFilterFavs: () -> Unit = { },
    onCityClick: (String) -> Unit = { },
    onInfoClick: (String) -> Unit = { },
    selectedId: String? = null
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            CityFilterBar(
                query = uiState.query,
                onQueryChange = onQueryChange,
                onFilterFavs = onFilterFavs,
                filterFavs = uiState.filterFav
            )
            if (uiState.loading) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                CityList(
                    uiState = uiState,
                    onFavClick = onFavClick,
                    onCityClick = onCityClick,
                    onInfoClick = onInfoClick,
                    selectedId = selectedId
                )
            }

        }
    }
}

@Composable
fun CityFilterBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onFilterFavs: () -> Unit = { },
    filterFavs: Boolean = false
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                modifier = Modifier
                    .padding(start = 12.dp, top = 8.dp, bottom = 8.dp)
                    .weight(1f),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                placeholder = { Text("Search city…") }
            )
            IconButton(
                onClick = onFilterFavs,
                modifier = Modifier
                    .padding(2.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = if (filterFavs) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun CityList(
    uiState: CityListUIState,
    onFavClick: (String) -> Unit = { },
    onCityClick: (String) -> Unit = { },
    onInfoClick: (String) -> Unit = { },
    selectedId: String? = null
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            items(uiState.cities) { city ->
                Column {
                    CityItem(
                        city = city,
                        onFavClick = { onFavClick(city.id) },
                        onClick = { onCityClick(city.id) },
                        onInfoClick = { onInfoClick(city.id) },
                        isSelected = city.id == selectedId
                    )
                    HorizontalDivider()
                }

            }
        }
    }

}

@Composable
fun CityItem(
    city: City,
    onFavClick: () -> Unit = { },
    onClick: () -> Unit = { },
    onInfoClick: () -> Unit = { },
    isSelected: Boolean = false
) {
    Row (
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(2.dp)
            .background(
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(4.dp),
            )
    ){
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${city.name}, ${city.country}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(2.dp)
            )
            Text(
                text = "${city.coordinates.latitude}, ${city.coordinates.longitude}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(2.dp)
            )
        }
        IconButton(
            onClick = onInfoClick,
            modifier = Modifier.padding(2.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = Color.Blue
            )
        }
        IconButton(
            onClick = onFavClick,
            modifier = Modifier.padding(2.dp)
        ) {
            Icon(
                imageVector = if (city.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = Color.Red
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CityItemPreview() {
    CityItem(
        city = City("id", "Cordoba", "AR", Coordinates(1.0, 2.0), true),
        isSelected = true
    )
}

@Preview
@Composable
private fun IndexFilterBarPreview() {
    CityFilterBar(
        query = "test",
        onQueryChange = { },
    )
}

@Preview
@Composable
private fun CityListPreview() {
    CityList(
        uiState = CityListUIState(
            cities = listOf(
                City("id", "Name", "CT", Coordinates(1.0, 2.0), true),
                City("id", "Name", "CT", Coordinates(1.0, 2.0), false),
                City("id", "Name", "CT", Coordinates(1.0, 2.0), true),
                City("id", "Name", "CT", Coordinates(1.0, 2.0), false),
            )
        )
    )

}

@Preview
@Composable
private fun CityScreenPreview() {
    CitiesScreenContent(
        uiState = CityListUIState(
            cities = listOf(
                City("id", "Name", "CT", Coordinates(1.0, 2.0), true),
                City("id", "Name", "CT", Coordinates(1.0, 2.0), false),
                City("id", "Name", "CT", Coordinates(1.0, 2.0), true),
                City("id", "Name", "CT", Coordinates(1.0, 2.0), false),
            )
        ),
    )
}