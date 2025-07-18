package com.themarto.cities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.themarto.cities.ui.theme.CitiesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CitiesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Cities(
                        listOf(
                            City("id", "name", "country", Coordinates(1.0, 2.0)),
                            City("id", "name", "country", Coordinates(1.0, 2.0)),
                            City("id", "name", "country", Coordinates(1.0, 2.0)),
                            City("id", "name", "country", Coordinates(1.0, 2.0)),
                            City("id", "name", "country", Coordinates(1.0, 2.0)),
                            City("id", "name", "country", Coordinates(1.0, 2.0)),
                            City("id", "name", "country", Coordinates(1.0, 2.0)),
                            City("id", "name", "country", Coordinates(1.0, 2.0)),
                            City("id", "name", "country", Coordinates(1.0, 2.0)),
                            City("id", "name", "country", Coordinates(1.0, 2.0)),
                            City("id", "name", "country", Coordinates(1.0, 2.0)),
                        ),
                        Modifier.padding(innerPadding).fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun Cities(
    cities: List<City>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(cities) { city ->
            Text(text = city.name)
            Text(text = city.country)
            Text(text = "${city.coordinates.latitude}, ${city.coordinates.longitude}")
        }
    }
}