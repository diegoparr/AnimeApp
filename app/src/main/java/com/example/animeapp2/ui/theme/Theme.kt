package com.example.animeapp2.ui.theme

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
import com.example.animeappjetpackcomposee.ui.theme.BloodRed
import com.example.animeappjetpackcomposee.ui.theme.DarkBackground
import com.example.animeappjetpackcomposee.ui.theme.DarkSurface
import com.example.animeappjetpackcomposee.ui.theme.DarkText
import com.example.animeappjetpackcomposee.ui.theme.GhostWhite
import com.example.animeappjetpackcomposee.ui.theme.LightBackground
import com.example.animeappjetpackcomposee.ui.theme.LightSurface

private val DarkColorScheme = darkColorScheme(
    primary = BloodRed,           // El color de acción (botones, selección)
    background = DarkBackground,  // El fondo de toda la pantalla
    surface = DarkSurface,        // El fondo de las tarjetas de manga
    onPrimary = Color.White,      // Color del texto cuando está sobre un botón rojo
    onBackground = GhostWhite,    // Color del texto sobre el fondo negro
    onSurface = GhostWhite        // Color del texto sobre las tarjetas
)

private val LightColorScheme = lightColorScheme(
    primary = BloodRed,           // Mantenemos el rojo para los botones
    background = LightBackground,  // Fondo claro
    surface = LightSurface,        // Tarjetas blancas
    onPrimary = Color.White,      // Texto blanco sobre botón rojo
    onBackground = DarkText,       // Texto oscuro sobre fondo claro
    onSurface = DarkText           // Texto oscuro sobre tarjetas
)

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */

@Composable
fun AnimeApp2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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