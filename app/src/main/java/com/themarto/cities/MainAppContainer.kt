package com.themarto.cities

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.themarto.cities.ui.theme.CitiesTheme
import com.themarto.features.cityList.CitiesScreen

@Composable
fun MainAppContainer() {
    CitiesTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavHost(
                modifier = Modifier.padding(innerPadding),
                navController = rememberNavController(),
                startDestination = "cities"
            ) {
                composable("cities") {
                    CitiesScreen()
                }
            }
        }
    }
}