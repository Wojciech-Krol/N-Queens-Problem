package com.android.nqueensproblem.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ChessComBtn,
    secondary = ChessComBtn,
    tertiary = ChessComBtn,
    background = ChessComMain,
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = ChessComMain,
    surfaceVariant = Color.White,
    onSurfaceVariant = ChessComMain,
    primaryContainer = ChessComBtn,
    surfaceContainer = ChessComBtn,
    error = ErrorColor
)

private val LightColorScheme = lightColorScheme(
    primary = ChessComBtn,
    secondary = ChessComBtn,
    tertiary = ChessComBtn,
    background = ChessComMain,
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = ChessComMain,
    surfaceVariant = Color.White,
    onSurfaceVariant = ChessComMain,
    primaryContainer = ChessComBtn,
    surfaceContainer = ChessComBtn,
    error = ErrorColor
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun NQueensProblemTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}