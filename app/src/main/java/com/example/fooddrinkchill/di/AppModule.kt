package com.example.fooddrinkchill.di

import com.example.fooddrinkchill.data.repository.PreferenceRepository
import com.example.fooddrinkchill.screen.welcome.WelcomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { PreferenceRepository(get()) }
    viewModel { WelcomeViewModel(get())  }
}