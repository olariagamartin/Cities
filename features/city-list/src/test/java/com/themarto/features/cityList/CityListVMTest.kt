package com.themarto.features.cityList

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.PagingDataEvent
import androidx.paging.PagingDataPresenter
import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.themarto.core.data.utils.Result
import com.themarto.core.domain.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
    fun `A0_WHEN ViewModel is initialized THEN loading is true`() = runTest {
        val vm = CityListViewModel(provideCityRepository())
        vm.uiState.test {
            assertEquals(true, awaitItem().loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `A1_WHEN cities are retrieved THEN loading is false`() = runTest {
        val vm = CityListViewModel(provideCityRepository())
        vm.uiState.test {
            awaitItem().cities?.first() // observe to trigger response
            assertEquals(false, awaitItem().loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `B0_WHEN cities are retrieved THEN they are displayed`() = runTest {
        val vm = CityListViewModel(provideCityRepository())
        vm.uiState.test {
            val cityList = awaitItem().cities?.asSnapshot {
                scrollTo(50)
            }
            assertEquals(provideCityList(), cityList)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `B1_WHEN getCitiesFiltered respond error THEN display error`() = runTest {
        val citiesFilteredFlow = flowOf<Result<PagingData<City>>>(Result.Error("error"))
        val viewModel = CityListViewModel(
            provideCityRepository(
                getCitiesFiltered = citiesFilteredFlow
            )
        )

        viewModel.uiState.test {
            awaitItem().let { uiState ->
                uiState.cities?.first() // observe to trigger response
                assertEquals("error", awaitItem().error)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `B2_WHEN PagingData is loading THEN append state is Loading`() = runTest {
        val vm = CityListViewModel(
            provideCityRepository(
                getCitiesFiltered = flowOf (
                    Result.Success(
                        PagingData.from(
                            data = provideCityList(),
                            sourceLoadStates = LoadStates(
                                refresh = LoadState.NotLoading(true),
                                prepend = LoadState.NotLoading(true),
                                append = LoadState.Loading // paging data loading
                            )
                        )
                    )
                )
            )
        )
        val pagingDataPresenter = object : PagingDataPresenter<City>() {
            override suspend fun presentPagingDataEvent(event: PagingDataEvent<City>) { }

        }
        vm.uiState.test {
            // start observing cities flow
            awaitItem().cities?.test {
                awaitItem().let { pagingDataPresenter.collectFrom(it) }
                cancelAndIgnoreRemainingEvents()
            }

            pagingDataPresenter.loadStateFlow.test {
                // assert that append state is loading
                assert(awaitItem()?.append is LoadState.Loading)
                cancelAndIgnoreRemainingEvents()
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `B3_WHEN Paging Data is not loading THEN append is NotLoading`() = runTest {
        val vm = CityListViewModel(
            provideCityRepository()
        )
        val pagingDataPresenter = object : PagingDataPresenter<City>() {
            override suspend fun presentPagingDataEvent(event: PagingDataEvent<City>) { }

        }
        vm.uiState.test {
            // start observing cities flow
            awaitItem().cities?.test {
                awaitItem().let { pagingDataPresenter.collectFrom(it) }
                cancelAndIgnoreRemainingEvents()
            }

            pagingDataPresenter.loadStateFlow.test {
                // assert that append state is not loading
                assert(awaitItem()?.append is LoadState.NotLoading)
                cancelAndIgnoreRemainingEvents()
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `C0_WHEN onQueryChange is called THEN getCitiesFiltered with prefix is called`() = runTest {
        val cityRepoMock = spy(provideCityRepository())
        val vm = CityListViewModel(cityRepoMock)

        vm.uiState.test {
            awaitItem().cities?.test {
                awaitItem() // initial emit
                vm.onQueryChanged("prefix")
                advanceUntilIdle()

                verify(cityRepoMock).getCitiesFiltered("prefix")
                cancelAndIgnoreRemainingEvents()
            }
            cancelAndIgnoreRemainingEvents()
        }

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

       vm.uiState.test {
           awaitItem().cities?.test {
               awaitItem() // initial emit
               vm.onFilterFavClick()
               advanceUntilIdle()

               verify(repo).getCitiesFiltered(any(), eq(true))
               cancelAndIgnoreRemainingEvents()
           }
           cancelAndIgnoreRemainingEvents()
       }
   }

    @Test
    fun `F0_WHEN is initialized THEN selectedCity is null`() = runTest {
        val vm = CityListViewModel(provideCityRepository())
        vm.uiState.test {
            assertEquals(null, awaitItem().selectedCity)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `F1_WHEN selectCity is called THEN selectedCity is updated`() = runTest {
        val vm = CityListViewModel(provideCityRepository())
        val city = provideCityList().first()
        vm.uiState.test {
            awaitItem() // initial emit
            vm.selectCity(city)
            assertEquals(city, awaitItem().selectedCity)
            cancelAndIgnoreRemainingEvents()
        }
    }

}