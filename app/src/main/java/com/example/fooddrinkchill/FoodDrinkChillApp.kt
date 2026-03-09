package com.example.fooddrinkchill

import android.app.Application
import com.example.fooddrinkchill.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FoodDrinkChillApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@FoodDrinkChillApp)
            modules(appModule)
        }
    }
}