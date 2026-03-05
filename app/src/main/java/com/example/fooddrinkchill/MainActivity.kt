package com.example.fooddrinkchill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.fooddrinkchill.navigation.NavigationRoot
import com.example.fooddrinkchill.ui.theme.FoodDrinkChillTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}