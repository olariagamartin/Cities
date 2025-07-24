package com.themarto.cities

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
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
    const val CITY_ID_ARG = "city_id"

    const val MAP = "map"
    const val MAP_ROUTE_PATTERN = "$MAP/{$CITY_ID_ARG}"

    const val CITY_DETAILS = "cityDetails"
    const val CITY_DETAILS_ROUTE_PATTERN = "$CITY_DETAILS/{$CITY_ID_ARG}"

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
                        navArgument(Destinations.CITY_ID_ARG) { type = NavType.StringType }
                    ),
                    enterTransition = {
                        slideIntoContainer(
                            animationSpec = tween(durationMillis = 500),
                            towards = AnimatedContentTransitionScope.SlideDirection.Left
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            animationSpec = tween(durationMillis = 500),
                            towards = AnimatedContentTransitionScope.SlideDirection.Right
                        )
                    }
                ) { backStackEntry ->
                    MapScreen(
                        onNavigateBack = { navController.popBackStack() },
                        cityId = backStackEntry.arguments?.getString(Destinations.CITY_ID_ARG)!!
                    )
                }

                composable(
                    route = Destinations.CITY_DETAILS_ROUTE_PATTERN,
                    arguments = listOf(
                        navArgument(Destinations.CITY_ID_ARG) { type = NavType.StringType }
                    ),
                    enterTransition = {
                        slideIntoContainer(
                            animationSpec = tween(durationMillis = 500),
                            towards = AnimatedContentTransitionScope.SlideDirection.Up
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            animationSpec = tween(durationMillis = 500),
                            towards = AnimatedContentTransitionScope.SlideDirection.Down
                        )
                    }
                ) { backStackEntry ->
                    CityDetailsScreen(
                        onNavigateBack = { navController.popBackStack() },
                        cityId = backStackEntry.arguments?.getString(Destinations.CITY_ID_ARG)!!
                    )
                }
            }
        }
    }
}