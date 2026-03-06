package com.example.fooddrinkchill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.fooddrinkchill.navigation.NavigationRoot
import com.example.fooddrinkchill.ui.theme.FoodDrinkChillTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        initSplashScreen(splashScreen)
        enableEdgeToEdge()
        setContent {
            FoodDrinkChillTheme {
                Scaffold { innerPadding ->
                    NavigationRoot(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun initSplashScreen(splashScreen: SplashScreen) {
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            splashScreenView.view.animate().alpha(0f).setDuration(500L).withEndAction {
                splashScreenView.remove()
            }.start()
        }
    }
}