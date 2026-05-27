package com.example.myapplication

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = NeonCrimson,
    secondary = CoolGray,
    tertiary = DeepBloodRed,
    background = MatteBlack,
    surface = DarkCarbon,
    onPrimary = PureWhite,
    onSecondary = MatteBlack,
    onTertiary = PureWhite,
    onBackground = PureWhite,
    onSurface = PureWhite,
    surfaceVariant = HeavyMetallic,
    onSurfaceVariant = CoolGray
  )

private val LightColorScheme = DarkColorScheme // AizenEA only supports premium Matte Black/Red theme!

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Force premium dark theme always!
  dynamicColor: Boolean = false, // Direct static brand identity control!
  content: @Composable () -> Unit,
) {
  val colorScheme = DarkColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
