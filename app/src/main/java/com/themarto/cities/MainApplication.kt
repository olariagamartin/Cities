package com.themarto.cities

import android.app.Application
import com.themarto.core.data.DataModuleProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(
                viewModelModule
                    .plus(DataModuleProvider.getModules())
            )
        }

    }
}