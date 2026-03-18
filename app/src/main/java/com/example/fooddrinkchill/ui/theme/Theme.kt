package com.example.fooddrinkchill.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Immutable
data class ExtendedColors(
    val primaryText: Color,
    val fixedPrimaryText: Color,
    val outline: Color,
    val outlineVariant: Color,
    val container: Color = Color.Unspecified,
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        primaryText = Color.Unspecified,
        fixedPrimaryText = Color.Unspecified,
        outline = Color.Unspecified,
        outlineVariant = Color.Unspecified,
        container = Color.Unspecified,
    )
}

@Composable
fun FoodDrinkChillTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val extendedColors = if (darkTheme) {
        ExtendedColors(
            primaryText = DarkText,
            fixedPrimaryText = OrangePrimary,
            outline = DarkOutline,
            outlineVariant = DarkOutlineVariant,
            container = DarkContainer,
        )
    } else {
        ExtendedColors(
            outline = LightOutline,
            primaryText = TextPrimary,
            outlineVariant = LightOutlineVariant,
            fixedPrimaryText = DarkOrangePrimary,
            container = LightContainer,
        )
    }

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = DarkOrangePrimary,
            secondary = DarkOrangeSecondary,
            background = DarkBackground,
            surface = DarkSurface,
            onPrimary = Color.Black,
            onSecondary = Color.Black,
            onBackground = DarkText,
            onSurface = DarkText
        )
    } else {
        lightColorScheme(
            primary = OrangePrimary,
            secondary = OrangeSecondary,
            background = LightBackground,
            surface = Color.White,
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = TextPrimary,
            onSurface = TextPrimary
        )
    }

    val finalColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> colorScheme
    }

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = finalColorScheme,
            typography = Typography,
            content = content
        )
    }
}

object ExtendedTheme {
    val colorScheme: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}
