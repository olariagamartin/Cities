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
import com.themarto.features.cityDetails.CityDetailsScreen
import com.themarto.features.cityList.CitiesScreen
import com.themarto.features.cityList.MapScreen

object Destinations {
    const val CITIES = "cities"

    const val MAP = "map"
    const val MAP_ARGUMENT = "city_id"
    const val MAP_ROUTE_PATTERN = "$MAP/{$MAP_ARGUMENT}"

    const val CITY_DETAILS = "cityDetails"
    const val CITY_DETAILS_ARGUMENT = "city_id"
    const val CITY_DETAILS_ROUTE_PATTERN = "$CITY_DETAILS/{$CITY_DETAILS_ARGUMENT}"

    fun mapRoute(cityId: String) = "$MAP/$cityId"

    fun cityDetailsRoute(cityId: String) = "$CITY_DETAILS/$cityId"
}

@Composable
fun MainAppContainer() {
    CitiesTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val navController = rememberNavController()
            NavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                startDestination = Destinations.CITIES
            ) {
                composable(
                    route = Destinations.CITIES
                ) {
                    CitiesScreen(
                        navigateToCityMap = { cityId ->
                            navController.navigate(Destinations.mapRoute(cityId))
                        },
                        navigateToCityDetails = { cityId ->
                            navController.navigate(Destinations.cityDetailsRoute(cityId))
                        }
                    )
                }

                composable(
                    route = Destinations.MAP_ROUTE_PATTERN,
                    arguments = listOf(
                        navArgument("city_id") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    MapScreen(
                        onNavigateBack = { navController.popBackStack() },
                        cityId = backStackEntry.arguments?.getString("city_id")!!
                    )
                }

                composable(
                    route = Destinations.CITY_DETAILS_ROUTE_PATTERN,
                    arguments = listOf(
                        navArgument("city_id") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    CityDetailsScreen(
                        onNavigateBack = { navController.popBackStack() },
                        cityId = backStackEntry.arguments?.getString("city_id")!!
                    )
                }
            }
        }
    }
}