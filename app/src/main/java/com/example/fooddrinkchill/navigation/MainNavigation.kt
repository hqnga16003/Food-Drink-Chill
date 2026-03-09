package com.example.fooddrinkchill.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier
) {
    val navigationState = rememberNavigationState(
        startRoute = Route.Main.Home, topLevelRoutes = TOP_LEVEL_DESTINATIONS.keys
    )
    val navigator = remember {
        Navigator(navigationState)
    }
    Scaffold(
        modifier = modifier, bottomBar = {
            MainNavigationBar(
                selectKey = navigationState.topLevelRoute, onSelectKey = { navigator.navigate(it) })
        }) { innerPadding ->
        NavDisplay(
            onBack = navigator::goBack,
            entries = navigationState.toEntries(entryProvider {
                entry<Route.Main.Home> {
                    Text("Home Screen")
                }
                entry<Route.Main.Search> {
                    Text("Search Screen")
                }
                entry<Route.Main.Profile> {
                    Text("Profile Screen")
                }
            }),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}


@Composable
fun MainNavigationBar(
    selectKey: NavKey, onSelectKey: (NavKey) -> Unit, modifier: Modifier = Modifier
) {
    BottomAppBar(modifier = modifier) {
        TOP_LEVEL_DESTINATIONS.forEach { (route, item) ->
            NavigationBarItem(selected = route == selectKey, onClick = {
                onSelectKey(route)
            }, label = { Text(item.title) }, icon = {
                Icon(
                    painter = painterResource(item.icon), contentDescription = item.title
                )
            })
        }
    }
}
