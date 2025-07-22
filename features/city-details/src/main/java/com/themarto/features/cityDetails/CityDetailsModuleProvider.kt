package com.themarto.features.cityDetails

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object CityDetailsModuleProvider {
    fun getModules(): List<Module> = listOf(
        viewModelModule
    )
}

private val viewModelModule = module {
    viewModel { (cityId: String) ->
        CityDetailViewModel(
            cityId = cityId,
            cityRepository = get()
        )
    }

}