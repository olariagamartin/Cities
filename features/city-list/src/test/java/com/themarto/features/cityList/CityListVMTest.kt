package com.themarto.features.cityList

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
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify

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

    @Test
    fun `A3_WHEN filterByPrefix is called THEN getCities with prefix is called`() = runTest {
        val cityRepoMock = spy(provideCityRepository())
        val vm = CityListViewModel(cityRepoMock)

        advanceUntilIdle()

        vm.filterByPrefix("prefix")

        advanceUntilIdle()

        verify(cityRepoMock).getCitiesFiltered("prefix")
    }

    // ------ Test help methods ----------
    private fun provideCityRepository(
        getCities: suspend () -> Result<List<City>> = { Result.Success(provideCityList()) }
    ): CityRepository {
        return object : CityRepository {
            override suspend fun getCities(): Result<List<City>> {
                return getCities()
            }

            override suspend fun getCitiesFiltered(prefix: String): Result<List<City>> {
                return Result.Success(provideCityList().filter { it.name.startsWith(prefix) })
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