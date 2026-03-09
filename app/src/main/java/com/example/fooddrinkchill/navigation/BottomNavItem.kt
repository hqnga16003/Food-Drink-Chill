package com.example.fooddrinkchill.navigation

import com.example.fooddrinkchill.R


data class BottomNavItem(
    val icon:  Int,
    val title: String,
)

val TOP_LEVEL_DESTINATIONS = mapOf(
    Route.Main.Home to BottomNavItem(
        icon = R.drawable.home,
        title = "Home"
    ),
    Route.Main.Search to BottomNavItem(
        icon = R.drawable.search,
        title = "Search"
    ),
    Route.Main.Profile to BottomNavItem(
        icon = R.drawable.profile,
        title = "Profile"
    ),
)