package com.themarto.features.cityDetails

import app.cash.turbine.test
import com.themarto.core.data.repository.CityRepository
import com.themarto.core.data.utils.Result
import com.themarto.core.domain.City
import com.themarto.core.domain.Coordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class CityDetailVMTest {

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
        val vm = CityDetailViewModel(
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

        val vm = CityDetailViewModel(
            cityRepository = repo,
            cityId = cityId
        )

        advanceUntilIdle()

        verify(repo).getCityById(cityId)

    }

    @Test
    fun `B1_WHEN getCityById respond success THEN city is displayed`() = runTest {
        val vm = CityDetailViewModel(
            cityRepository = provideCityRepository(),
            cityId = "123"
        )
        vm.uiState.test {
            awaitItem() // initial emit
            assertEquals(provideCityList().first(), awaitItem().city)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `C0_WHEN onFavoriteClick THEN toggleFavorite is called`() = runTest {
        val repo = spy(provideCityRepository())
        val vm = CityDetailViewModel(
            cityRepository = repo,
            cityId = "123"
        )

        vm.onFavoriteClick()

        advanceUntilIdle()

        verify(repo).toggleFavorite("123")
    }

    @Test
    fun `C1_WHEN onFavoriteClick THEN getCityById is called again`() = runTest {
        val repo = spy(provideCityRepository())
        val vm = CityDetailViewModel(
            cityRepository = repo,
            cityId = "123"
        )

        vm.onFavoriteClick()

        advanceUntilIdle()

        verify(repo, times(2)).getCityById("123")
    }

    // ----------- helper functions --------------------
    // duplicated: could be in a separate test module
    private fun provideCityRepository(
        getCitiesFiltered: Flow<Result<List<City>>> = flow {
            emit(Result.Success(provideCityList()))
        }
    ): CityRepository {
        return object : CityRepository {

            override suspend fun getCitiesFiltered(prefix: String, filterFav: Boolean): Flow<Result<List<City>>> {
                return getCitiesFiltered
            }

            override suspend fun toggleFavorite(id: String) {
                // nothing
            }

            override suspend fun getCityById(id: String): Flow<Result<City>> = flow {
                emit(Result.Success(provideCityList().first { it.id == id }))
            }
        }
    }

    fun provideCityList() : List<City> {
        return listOf(
            City("123", "name", "country", Coordinates(1.0, 2.0), false),
            City("124", "name", "country", Coordinates(1.0, 2.0), false),
            City("125", "name", "country", Coordinates(1.0, 2.0), false),
        )
    }
}