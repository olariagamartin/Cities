package com.themarto.features.cityList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.themarto.core.domain.City
import com.themarto.core.domain.Coordinates
import org.koin.androidx.compose.koinViewModel

@Composable
fun CitiesScreen(
    viewModel: CityListViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CitiesScreenContent(
        uiState = uiState,
        onQueryChange = viewModel::onQueryChanged,
    )
}

@Composable
fun CitiesScreenContent(
    uiState: CityListUIState,
    onQueryChange: (String) -> Unit,
) {
    Column {
        CityFilterBar(
            query = uiState.query,
            onQueryChange = onQueryChange,
        )
        CityList(
            uiState = uiState
        )
    }
}

@Composable
fun CityFilterBar(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            placeholder = { Text("Search city…") }
        )
    }
}

@Composable
fun CityList(
    uiState: CityListUIState,
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            items(uiState.cities) { city ->
                Text(text = city.name)
                Text(text = city.country)
                Text(text = "${city.coordinates.latitude}, ${city.coordinates.longitude}")
            }
        }
    }

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
                City("id", "name", "country", Coordinates(1.0, 2.0), false),
                City("id", "name", "country", Coordinates(1.0, 2.0), false),
                City("id", "name", "country", Coordinates(1.0, 2.0), false),
                City("id", "name", "country", Coordinates(1.0, 2.0), false),
            )
        )
    )

}