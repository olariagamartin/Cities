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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
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

    if (isLandscape) {
        CityListWithMap(
            uiState = uiState,
            onQueryChange = viewModel::onQueryChanged,
            onFavClick = viewModel::onFavClick,
            onFilterFavs = viewModel::onFilterFavClick,
            onCityClick = { viewModel.selectCity(it) },
            onInfoClick = navigateToCityDetails,
            selectedCity = uiState.selectedCity
        )
    } else {
        CitiesScreenContent(
            uiState = uiState,
            onQueryChange = viewModel::onQueryChanged,
            onFavClick = viewModel::onFavClick,
            onFilterFavs = viewModel::onFilterFavClick,
            onCityClick = { navigateToCityMap(it.id) },
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
    onCityClick: (City) -> Unit = { },
    onInfoClick: (String) -> Unit = { },
    selectedCity: City? = null
) {
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
                selectedId = selectedCity?.id
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
    onCityClick: (City) -> Unit = { },
    onInfoClick: (String) -> Unit = { },
    selectedId: String? = null
) {
    val cities = uiState.cities?.collectAsLazyPagingItems()
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
            if (cities == null || cities.itemCount == 0 && cities.loadState.isIdle) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No cities found")
                }
            }
            else {
                CityList(
                    cities = cities,
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
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
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
                placeholder = { Text("Search city…") },
                shape = RoundedCornerShape(24.dp)
            )
            IconButton(
                onClick = onFilterFavs,
                modifier = Modifier
                    .padding(2.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = if (filterFavs) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Composable
fun CityList(
    cities: LazyPagingItems<City>,
    onFavClick: (String) -> Unit = { },
    onCityClick: (City) -> Unit = { },
    onInfoClick: (String) -> Unit = { },
    selectedId: String? = null
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            items(cities.itemCount) { index ->
                Column {
                    cities[index]?.let {
                        CityItem(
                            city = it,
                            onFavClick = { onFavClick(it.id) },
                            onClick = { onCityClick(it) },
                            onInfoClick = { onInfoClick(it.id) },
                            isSelected = it.id == selectedId
                        )
                    }
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
        AsyncImage(
            modifier = Modifier.size(48.dp).align(Alignment.CenterVertically),
            model = "https://flagsapi.com/${city.country}/flat/64.png",
            contentDescription = "Recipe image",
            placeholder = ColorPainter(Color.Gray),
        )
        Column(
            modifier = Modifier.weight(1f).padding(start = 8.dp)
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
                tint = Color.LightGray
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
    val lazyItems = previewPagingFlow().collectAsLazyPagingItems()
    CityList(
        cities = lazyItems
    )

}

@Preview
@Composable
private fun CityScreenPreview() {
    val lazyItems = previewPagingFlow()
    CitiesScreenContent(
        uiState = CityListUIState(
            cities = lazyItems
        )
    )
}
