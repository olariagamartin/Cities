package com.themarto.cities

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.themarto.cities.ui.theme.CitiesTheme
import com.themarto.features.cityList.CitiesScreen
import com.themarto.features.cityList.MapScreen

@Composable
fun MainAppContainer() {
    CitiesTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val navController = rememberNavController()
            NavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                startDestination = "cities"
            ) {
                composable(
                    route = "cities"
                ) {
                    CitiesScreen(
                        onCityClick = {
                            navController.navigate("map/$it")
                        }
                    )
                }

                composable(
                    route = "map/{city_id}",
                    arguments = listOf(
                        navArgument("city_id") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    MapScreen(
                        onNavigateBack = { navController.popBackStack() },
                        cityId = backStackEntry.arguments?.getString("city_id")!!
                    )
                }
            }
        }
    }
}