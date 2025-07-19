package com.themarto.cities

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        CityListViewModel(get())
    }
}

// TODO: Add real implementation
val repositoryModule = module {
    single<CityRepository> {
        object : CityRepository {
            override suspend fun getCities(): Result<List<City>> {
                return Result.Success(
                    listOf(
                        City("id", "name", "country", Coordinates(1.0, 2.0)),
                        City("id", "name", "country", Coordinates(1.0, 2.0)),
                        City("id", "name", "country", Coordinates(1.0, 2.0)),
                        City("id", "name", "country", Coordinates(1.0, 2.0)),
                        City("id", "name", "country", Coordinates(1.0, 2.0)),
                        City("id", "name", "country", Coordinates(1.0, 2.0)),
                        City("id", "name", "country", Coordinates(1.0, 2.0)),
                        City("id", "name", "country", Coordinates(1.0, 2.0)),
                        City("id", "name", "country", Coordinates(1.0, 2.0)),
                        City("id", "name", "country", Coordinates(1.0, 2.0)),
                        City("id", "name", "country", Coordinates(1.0, 2.0)),
                        City("id", "name", "country", Coordinates(1.0, 2.0)),
                        City("id", "name", "country", Coordinates(1.0, 2.0)),
                        City("id", "name", "country", Coordinates(1.0, 2.0)),
                        City("id", "name", "country", Coordinates(1.0, 2.0)),
                        City("id", "name", "country", Coordinates(1.0, 2.0)),
                        City("id", "name", "country", Coordinates(1.0, 2.0)),
                        City("id", "name", "country", Coordinates(1.0, 2.0)),
                    )
                )
            }

        }
    }
}