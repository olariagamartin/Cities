package com.themarto.core.data

import com.themarto.core.data.network.CityNetworkApi
import com.themarto.core.data.network.createRetrofit
import com.themarto.core.data.repository.CityRepository
import com.themarto.core.data.repository.CityRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit

object DataModuleProvider {
    fun getModules(): List<Module> = listOf(
        repositoryModule,
        networkModule
    )
}

private val repositoryModule = module {
    single<CityRepository> {
        CityRepositoryImpl(get<Retrofit>().create(CityNetworkApi::class.java))
    }
}

private val networkModule = module {
    single<Retrofit> {
        createRetrofit()
    }
}