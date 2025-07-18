package com.themarto.cities

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.yield
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CityListVMTest {

    private val testDispatcher = StandardTestDispatcher()

    private val cityRepository = object : CityRepository {
        override suspend fun getCities(): List<City> {
            delay(1000) // simulate network delay
            return provideCityList()
        }
    }

    private lateinit var vm: CityListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        vm = CityListViewModel(cityRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `A0_WHEN ViewModel is initialized THEN cities are empty`() = runTest {
        vm.uiState.test {
            assertEquals(emptyList<City>(), awaitItem().cities)
            cancelAndIgnoreRemainingEvents()
        }

    }

    @Test
    fun `A1_WHEN cities are retrieved THEN they are displayed`() = runTest {
        vm.uiState.test {
            awaitItem() // initial emit
            advanceUntilIdle()
            assertEquals(provideCityList(), awaitItem().cities)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun provideCityList() : List<City> {
        return listOf(
            City("id", "name", "country", Coordinates(1.0, 2.0)),
            City("id", "name", "country", Coordinates(1.0, 2.0)),
            City("id", "name", "country", Coordinates(1.0, 2.0)),
        )
    }

}