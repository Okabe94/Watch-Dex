package com.watch_dex.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

val primary = Color(0xFF3897F3)
val confirmColor = Color(0xFF3AC240)
val Teal200 = Color(0xFF03DAC5)
val Red400 = Color(0xFFCF6679)

internal val wearColorPalette: Colors = Colors(
    primary = primary,
    primaryVariant = confirmColor,
    secondary = Teal200,
    secondaryVariant = Teal200,
    error = Red400,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onError = Color.Black,
    onBackground = Color.White
)
