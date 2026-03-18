package com.example.fooddrinkchill.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.fooddrinkchill.R

// Light Theme Colors
val OrangePrimary @Composable get() = colorResource(id = R.color.orange_primary)
val OrangeSecondary @Composable get() = colorResource(id = R.color.orange_secondary)
val LightBackground @Composable get() = colorResource(id = R.color.light_background)
val TextPrimary @Composable get() = colorResource(id = R.color.text_primary)

val LightOutline @Composable get() = colorResource(id = R.color.light_outline)

val LightOutlineVariant @Composable get() = colorResource(id = R.color.light_outline_variant)
val LightContainer @Composable get() = colorResource(id = R.color.light_container)


// Dark Theme Colors
val DarkOrangePrimary @Composable get() = colorResource(id = R.color.dark_orange_primary)
val DarkOrangeSecondary @Composable get() = colorResource(id = R.color.dark_orange_secondary)
val DarkBackground @Composable get() = colorResource(id = R.color.dark_background)
val DarkText @Composable get() = colorResource(id = R.color.dark_text)
val DarkSurface @Composable get() = colorResource(id = R.color.dark_surface)

val DarkOutline @Composable get() = colorResource(id = R.color.dark_outline)

val DarkOutlineVariant @Composable get() = colorResource(id = R.color.dark_outline_variant)
val DarkContainer @Composable get() = colorResource(id = R.color.dark_container)


// Standard
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
