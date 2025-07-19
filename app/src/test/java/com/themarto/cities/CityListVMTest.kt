package com.themarto.cities

import app.cash.turbine.test
import com.themarto.core.data.repository.CityRepository
import com.themarto.core.data.utils.Result
import com.themarto.core.domain.City
import com.themarto.core.domain.Coordinates
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

@ExperimentalCoroutinesApi
class CityListVMTest {

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
    fun `A0_WHEN ViewModel is initialized THEN cities are empty`() = runTest {
        val vm = CityListViewModel(provideCityRepository())
        vm.uiState.test {
            assertEquals(emptyList<City>(), awaitItem().cities)
            cancelAndIgnoreRemainingEvents()
        }

    }

    @Test
    fun `A1_WHEN cities are retrieved THEN they are displayed`() = runTest {
        val vm = CityListViewModel(provideCityRepository())
        vm.uiState.test {
            awaitItem() // initial emit
            advanceUntilIdle()
            assertEquals(provideCityList(), awaitItem().cities)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `A2_WHEN getCities respond error THEN display error`() = runTest {
        val viewModel = CityListViewModel(
            provideCityRepository { Result.Error("error") }
        )

        viewModel.uiState.test {
            awaitItem() // initial emit
            advanceUntilIdle()
            assertEquals("error", awaitItem().error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ------ Test help methods ----------
    private fun provideCityRepository(
        getCities: suspend () -> Result<List<City>> = { Result.Success(provideCityList()) }
    ): CityRepository {
        return object : CityRepository {
            override suspend fun getCities(): Result<List<City>> {
                return getCities()
            }
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