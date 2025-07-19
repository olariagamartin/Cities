package com.themarto.features.cityList

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object CityListModuleProvider {
    fun getModules(): List<Module> = listOf(
        viewModelModule
    )
}

private val viewModelModule = module {
    viewModel {
        CityListViewModel(get())
    }
}