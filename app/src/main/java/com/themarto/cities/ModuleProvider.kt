package com.themarto.cities

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val viewModelModule = module {
    viewModel {
        CityListViewModel(get())
    }
}

val repositoryModule = module {
    single<CityRepository> {
        CityRepositoryImpl(get<Retrofit>().create(CityNetworkApi::class.java))
    }
}

val networkModule = module {
    single<Retrofit> {
        createRetrofit()
    }
}