package com.example.fooddrinkchill.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.fooddrinkchill.navigation.Route
import com.example.fooddrinkchill.screen.HomeScreen
import com.example.fooddrinkchill.screen.auth.LoginScreen
import com.example.fooddrinkchill.screen.auth.RegisterScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic


@Composable
fun AuthNavigation(
    onLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val authBackStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.Auth.Login::class, Route.Auth.Login.serializer())
                    subclass(Route.Auth.Register::class, Route.Auth.Register.serializer())
                }
            }
        },
        Route.Auth.Login
    )
    NavDisplay(
        backStack = authBackStack,
        modifier = modifier,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Route.Auth.Login> {
                LoginScreen(onLoginSuccess = onLogin, onRegisterClick = {
                    authBackStack.add(Route.Auth.Register)
                })
            }
            entry<Route.Auth.Register> {
                RegisterScreen(onBackToLogin = {
                    authBackStack.remove(Route.Auth.Register)
                }, onRegisterSuccess = onLogin)
            }
        }
    )
}