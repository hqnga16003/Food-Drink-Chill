package com.example.fooddrinkchill.di

import com.example.fooddrinkchill.data.repository.FirebaseRepository
import com.example.fooddrinkchill.data.repository.PreferenceRepository
import com.example.fooddrinkchill.screen.auth.LoginViewModel
import com.example.fooddrinkchill.screen.auth.RegisterViewModel
import com.example.fooddrinkchill.screen.welcome.WelcomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { PreferenceRepository(get()) }
    single { FirebaseRepository() }
    viewModel { WelcomeViewModel(get())  }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
}