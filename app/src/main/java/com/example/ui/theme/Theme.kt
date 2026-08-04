package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ZyraDarkColorScheme = darkColorScheme(
    primary = ZyraPrimary,
    secondary = ZyraSecondary,
    tertiary = ZyraSecondary,
    background = ZyraBackground,
    surface = ZyraSurface,
    surfaceVariant = ZyraSurfaceVariant,
    onPrimary = ZyraTextPrimary,
    onSecondary = ZyraTextPrimary,
    onBackground = ZyraTextPrimary,
    onSurface = ZyraTextPrimary,
    onSurfaceVariant = ZyraTextSecondary
)

@Composable
fun ZyraTheme(
    darkTheme: Boolean = true, // ZYRA is strictly dark-themed by design specification
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = ZyraDarkColorScheme,
        typography = Typography,
        content = content
    )
}

