package com.example.appcomapanion2.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// O app é tematicamente escuro (Darkest Dungeon), então usamos o MESMO
// esquema de cores pro modo claro e escuro — não faz sentido um Companion
// desse jogo ficar branco no modo claro do celular.
private val DarkColorScheme = darkColorScheme(
    primary = DungeonGold,
    onPrimary = DungeonOnGold,
    secondary = DungeonSurfaceAlt,
    onSecondary = DungeonGold,
    tertiary = DungeonGold,
    background = DungeonBackground,
    onBackground = DungeonOnDark,
    surface = DungeonSurface,
    onSurface = DungeonOnDark,
    error = DungeonError
)

private val LightColorScheme = DarkColorScheme

@Composable
fun AppComapanion2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color DESLIGADO: a identidade visual do Companion (dourado +
    // escuro) não pode depender do papel de parede do usuário.
    dynamicColor: Boolean = false,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
