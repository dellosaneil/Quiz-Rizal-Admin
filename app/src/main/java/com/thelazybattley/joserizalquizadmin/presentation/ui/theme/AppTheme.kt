package com.thelazybattley.joserizalquizadmin.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf


val LocalAppColors = staticCompositionLocalOf { lightAppColors }

val LocalAppTypography = staticCompositionLocalOf { getAppTypography() }


object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current
}

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        darkAppColors
    } else {
        lightAppColors
    }

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTypography provides getAppTypography(),
        content = content,
    )
}
