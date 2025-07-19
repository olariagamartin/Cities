package com.themarto.cities

import android.app.Application
import com.themarto.core.data.DataModuleProvider
import com.themarto.features.cityList.CityListModuleProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(
                CityListModuleProvider.getModules()
                    .plus(DataModuleProvider.getModules())
            )
        }

    }
}