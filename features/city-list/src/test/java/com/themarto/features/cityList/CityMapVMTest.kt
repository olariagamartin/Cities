package com.themarto.features.cityList

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class CityMapVMTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `A0_WHEN ViewModel is initialized THEN city is null`() = runTest {
        val vm = CityMapViewModel(
            cityRepository = provideCityRepository(),
            cityId = "123"
        )

        vm.uiState.test {
            assertEquals(null, awaitItem().city)
            cancelAndIgnoreRemainingEvents()
        }

    }


    @Test
    fun `B0_WHEN ViewModel is initialized THEN getCityById is called`() = runTest {
        val repo = spy(provideCityRepository())
        val cityId = "123"

        val vm = CityMapViewModel(
            cityRepository = repo,
            cityId = cityId
        )

        advanceUntilIdle()

        verify(repo).getCityById(cityId)

    }

    @Test
    fun `B1_WHEN getCityById respond success THEN city is displayed`() = runTest {
        val vm = CityMapViewModel(
            cityRepository = provideCityRepository(),
            cityId = "123"
        )
        vm.uiState.test {
            awaitItem() // initial emit
            assertEquals(provideCityList().first(), awaitItem().city)
            cancelAndIgnoreRemainingEvents()
        }
    }
}