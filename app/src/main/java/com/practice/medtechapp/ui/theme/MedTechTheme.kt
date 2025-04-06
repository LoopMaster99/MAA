package com.practice.medtechapp.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

val PrimaryDark = Color(0xFF2D2D2D)
val PrimaryLight = Color(0xFFD7C9AE)

val SecondaryDark = Color(0xFFA68763)
val SecondaryLight = Color(0xFFEAE0D2)

val BackgroundLight = Color(0xFFFAF6F0)
val TextColor = Color(0xFF2D2D2D)


// Additional Color Variations
val AccentColor = Color(0xFF8B4513)
val SuccessColor = Color(0xFF4CAF50)
val ErrorColor = Color(0xFFFF5722)
val WarningColor = Color(0xFFFFC107)

// Extended Theme Colors
val BackgroundDark = Color(0xFFE0D5C1)
val TextSecondary = Color(0xFF6E6E6E)

// Card and Container Colors
val CardBackground = Color(0xFFF5F0E6)
val CardBorder = Color(0xFFD3C1A8)

// State Colors
val ActiveColor = SecondaryDark
val InactiveColor = TextSecondary


// Define typography
val AppTypography = Typography(
    titleLarge = TextStyle(
        fontSize = 24.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp
    )
)

@Composable
fun SetStatusBarColor(color: Color, darkIcons: Boolean = true) {
    val context = LocalContext.current
    val activity = context as? Activity ?: return
    val window = activity.window

    WindowCompat.setDecorFitsSystemWindows(window, true)

    val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
    windowInsetsController.isAppearanceLightStatusBars = darkIcons

}

@Composable
fun MyMedTechTheme(content: @Composable () -> Unit) {
    SetStatusBarColor(color = PrimaryDark)

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = PrimaryDark,
            secondary = SecondaryDark,
            background = BackgroundLight,
            surface = CardBackground,
            error = ErrorColor,
            onPrimary = PrimaryLight,
            onSecondary = SecondaryLight,
            onBackground = TextColor,
            onSurface = TextColor
            //background = SecondaryLight

        ),
        typography = AppTypography,
        content = content
    )
}
