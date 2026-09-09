package com.example.ui.theme

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

enum class AppThemeSetting {
  SYSTEM,
  DARK,
  LIGHT,
}

private val DarkColorScheme =
    darkColorScheme(
        primary = EmeraldPrimary,
        onPrimary = EmeraldOnPrimary,
        primaryContainer = EmeraldContainerDark,
        onPrimaryContainer = EmeraldPrimary,
        secondary = PurpleKey,
        onSecondary = TextDarkPrimary,
        secondaryContainer = Color(0xFF2C164D),
        onSecondaryContainer = Color(0xFFE9D5FF),
        tertiary = CyanAccent,
        onTertiary = VaultDarkBg,
        background = VaultDarkBg,
        onBackground = TextDarkPrimary,
        surface = VaultDarkSurface,
        onSurface = TextDarkPrimary,
        surfaceVariant = VaultDarkSurfaceVariant,
        onSurfaceVariant = TextDarkSecondary,
        outline = VaultDarkBorder,
        error = RoseError,
        onError = TextDarkPrimary,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = EmeraldPrimaryDark,
        onPrimary = Color.White,
        primaryContainer = EmeraldContainerLight,
        onPrimaryContainer = EmeraldPrimaryDark,
        secondary = CyanAccentDark,
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFE0F2FE),
        onSecondaryContainer = Color(0xFF0369A1),
        tertiary = AmberWarning,
        onTertiary = Color.White,
        background = VaultLightBg,
        onBackground = TextLightPrimary,
        surface = VaultLightSurface,
        onSurface = TextLightPrimary,
        surfaceVariant = VaultLightSurfaceVariant,
        onSurfaceVariant = TextLightSecondary,
        outline = VaultLightBorder,
        error = RoseError,
        onError = Color.White,
    )

@Composable
fun SeedSafeTheme(
    themeSetting: AppThemeSetting = AppThemeSetting.SYSTEM,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
  val darkTheme =
      when (themeSetting) {
        AppThemeSetting.SYSTEM -> isSystemInDarkTheme()
        AppThemeSetting.DARK -> true
        AppThemeSetting.LIGHT -> false
      }

  val colorScheme =
      when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
          val context = LocalContext.current
          if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
      }

  MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content,
  )
}
