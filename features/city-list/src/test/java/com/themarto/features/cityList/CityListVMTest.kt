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
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.spy
import org.mockito.kotlin.times
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
    fun `B0_WHEN cities are retrieved THEN they are displayed`() = runTest {
        val vm = CityListViewModel(provideCityRepository())
        vm.uiState.test {
            awaitItem() // initial emit
            assertEquals(provideCityList(), awaitItem().cities)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `B1_WHEN getCitiesFiltered respond error THEN display error`() = runTest {
        val viewModel = CityListViewModel(
            provideCityRepository { _, _ -> Result.Error("error") }
        )

        viewModel.uiState.test {
            awaitItem() // initial emit
            assertEquals("error", awaitItem().error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `B2_WHEN getCitiesFiltered has not responded THEN loading is true`() = runTest {
        val vm = CityListViewModel(provideCityRepository())
        vm.uiState.test {
            assertEquals(true, awaitItem().loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `B3_WHEN getCitiesFiltered has responded THEN loading is false`() = runTest {
        val vm = CityListViewModel(provideCityRepository())
        vm.uiState.test {
            awaitItem() // initial emit
            assertEquals(false, awaitItem().loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `C0_WHEN onQueryChange is called THEN getCitiesFiltered with prefix is called`() = runTest {
        val cityRepoMock = spy(provideCityRepository())
        val vm = CityListViewModel(cityRepoMock)

        vm.onQueryChanged("prefix")

        advanceUntilIdle()

        verify(cityRepoMock).getCitiesFiltered("prefix")
    }

    @Test
    fun `C1_WHEN onQueryChange is called THEN query is updated`()  = runTest {
        val vm = CityListViewModel(provideCityRepository())

        vm.onQueryChanged("prefix1")

        vm.onQueryChanged("prefix2")

        advanceUntilIdle()

        vm.uiState.test {
            assertEquals("prefix2", awaitItem().query)
            cancelAndIgnoreRemainingEvents()
        }


    }

    @Test
    fun `D0_WHEN onFavClick is called THEN toggleFavorite is called`() = runTest {
        val repo = spy(provideCityRepository())
        val vm = CityListViewModel(repo)

        val city = provideCityList().first()
        vm.onFavClick(city.id)
        advanceUntilIdle()

        verify(repo).toggleFavorite(city.id)

    }

    @Test
    fun `D1_WHEN onFavClick is called THEN getCitiesFiltered is called again`() = runTest {
        val repo = spy(provideCityRepository())
        val vm = CityListViewModel(repo)

        val city = provideCityList().first()
        vm.onFavClick(city.id)
        advanceUntilIdle()

        verify(repo, times(2)).getCitiesFiltered(any(), any())

    }

    @Test
    fun `E0_WHEN onFilterFavClick called THEN filterFav is toggled`() = runTest {
        val vm = CityListViewModel(provideCityRepository())

        vm.uiState.test {
            assertEquals(false, awaitItem().filterFav)
            vm.onFilterFavClick()
            assertEquals(true, awaitItem().filterFav)
            cancelAndIgnoreRemainingEvents()
        }
    }

   @Test
   fun `E1_WHEN onFilterFavClick called THEN getCitiesFiltered is called with filterFav set to true`() = runTest {
       val repo = spy(provideCityRepository())
       val vm = CityListViewModel(repo)

       vm.onFilterFavClick()
       advanceUntilIdle()

       verify(repo).getCitiesFiltered(any(), eq(true))

   }

    // ------ Test help methods ----------
    private fun provideCityRepository(
        getCitiesFiltered: suspend (String, Boolean) -> Result<List<City>> = { prefix, filterFav ->
            Result.Success(provideCityList()
                .filter {
                    it.name.startsWith(prefix).and(!filterFav || it.isFavorite)
                })
        }
    ): CityRepository {
        return object : CityRepository {

            override suspend fun getCitiesFiltered(prefix: String, filterFav: Boolean): Result<List<City>> {
                return getCitiesFiltered(prefix, filterFav)
            }

            override suspend fun toggleFavorite(id: String) {
                // nothing
            }
        }
    }

    private fun provideCityList() : List<City> {
        return listOf(
            City("id", "name", "country", Coordinates(1.0, 2.0), false),
            City("id", "name", "country", Coordinates(1.0, 2.0), false),
            City("id", "name", "country", Coordinates(1.0, 2.0), false),
        )
    }

}