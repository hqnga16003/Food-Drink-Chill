package com.example.fooddrinkchill.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object Welcome : Route {

    }

    @Serializable
    data object Auth : Route {
        @Serializable
        data object Login : Route

        @Serializable

        data object Register : Route
    }

    @Serializable
    data object Main : Route {

        @Serializable
        data object Search : Route {

        }

        @Serializable
        data object Home : Route

        @Serializable
        data object Profile : Route
    }


}

