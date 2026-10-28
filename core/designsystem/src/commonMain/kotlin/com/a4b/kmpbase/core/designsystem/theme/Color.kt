package com.a4b.kmpbase.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Placeholder brand palette — replace with the app's design tokens.
private val Primary = Color(0xFFE5622B)
private val OnPrimary = Color(0xFFFFFFFF)
private val Secondary = Color(0xFF6B5B4E)
private val BackgroundLight = Color(0xFFFFFBF8)
private val SurfaceLight = Color(0xFFFFFFFF)
private val BackgroundDark = Color(0xFF16120F)
private val SurfaceDark = Color(0xFF221D19)
private val ErrorColor = Color(0xFFBA1A1A)

internal val LightColors = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    secondary = Secondary,
    background = BackgroundLight,
    surface = SurfaceLight,
    error = ErrorColor,
)

internal val DarkColors = darkColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    secondary = Color(0xFFD7C2B3),
    background = BackgroundDark,
    surface = SurfaceDark,
    error = Color(0xFFFFB4AB),
)
