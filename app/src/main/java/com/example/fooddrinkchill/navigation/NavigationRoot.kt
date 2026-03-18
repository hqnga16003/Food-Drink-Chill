package com.example.fooddrinkchill.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.fooddrinkchill.auth.AuthNavigation
import com.example.fooddrinkchill.data.repository.PreferenceRepository
import com.example.fooddrinkchill.screen.welcome.WelcomeScreen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.koinInject

@Composable
fun NavigationRoot(
    paddingValues: PaddingValues = PaddingValues()
) {
    val preferenceRepository: PreferenceRepository = koinInject()
    val isFirstTime = preferenceRepository.isFirstTimeLaunch()

    val startRoute = when {
        isFirstTime -> Route.Welcome
        FirebaseAuth.getInstance().currentUser != null -> Route.Main
        else -> Route.Auth
    }

    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.Welcome::class, Route.Welcome.serializer())
                    subclass(Route.Auth::class, Route.Auth.serializer())
                    subclass(Route.Main::class, Route.Main.serializer())

                }
            }
        }, startRoute
    )
    NavDisplay(
        backStack = backStack, entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ), entryProvider = entryProvider {

            entry<Route.Welcome> {
                WelcomeScreen {
                    backStack.remove(Route.Welcome)
                    backStack.add(Route.Auth)
                }
            }

            entry<Route.Auth> {
                AuthNavigation(
                    modifier = Modifier.padding(paddingValues),
                    onLogin = {
                        backStack.remove(Route.Auth)
                        backStack.add(Route.Main)
                    }
                )
            }

            entry<Route.Main> {
                MainNavigation()
            }
        })
}