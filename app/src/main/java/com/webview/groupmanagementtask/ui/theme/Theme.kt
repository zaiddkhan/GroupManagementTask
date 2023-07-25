package com.webview.groupmanagementtask.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary =  Color(0xFF9a4057),
    onPrimary = Color(0xFFffffff),
    primaryContainer = Color(0xFFffd9df),
    onPrimaryContainer = Color(0xFF3f0017),
    secondary = Color(0xFF75565c),
    onSecondary = Color(0xFFffffff),
    secondaryContainer = Color(0xFFffd9df),
    onSecondaryContainer = Color(0xFF2b151a),
    tertiary = Color(0XFF7a5732),
    onTertiary =  Color(0XFFffffff),
    tertiaryContainer = Color(0XFFffdcbc),
    onTertiaryContainer = Color(0XFF2c1700),
    error = Color(0XFFba1a1a),
    onError = Color(0XFFffffff),
    errorContainer = Color(0XFFffdad6),
    onErrorContainer = Color(0XFF410002),
    background = Color(0XFFfffbff),
    onBackground = Color(0XFF201a1b),
    surface = Color(0XFFfffbff),
    onSurface = Color(0XFF201a1b),
    outline = Color(0XFF847375),
    surfaceVariant = Color(0XFFf3dde0),
    onSurfaceVariant = Color(0XFF524345)
)

private val DarkColorScheme = darkColorScheme(
    primary =  Color(0XFFffb1c0),
    onPrimary = Color(0xFF5f112a),
    primaryContainer = Color(0xFF7c2940),
    onPrimaryContainer = Color(0xFFffd9df),
    secondary = Color(0xFFe4bdc3),
    onSecondary = Color(0xFF43292e),
    secondaryContainer = Color(0xFF5b3f44),
    onSecondaryContainer = Color(0xFFffd9df),
    tertiary = Color(0XFFecbe91),
    onTertiary =  Color(0XFF462a08),
    tertiaryContainer = Color(0XFF5f401d),
    onTertiaryContainer = Color(0XFFffdcbc),
    error = Color(0XFFffb4ab),
    onError = Color(0XFF690005),
    errorContainer = Color(0XFF93000a),
    onErrorContainer = Color(0XFFffdad6),
    background = Color(0XFF201a1b),
    onBackground = Color(0XFFece0e0),
    surface = Color(0XFF201a1b),
    onSurface = Color(0XFFece0e0),
    outline = Color(0XFF9f8c8f),
    surfaceVariant = Color(0XFF524345),
    onSurfaceVariant = Color(0XFFd6c2c4)
)

@Composable
fun GroupManagementTaskTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}