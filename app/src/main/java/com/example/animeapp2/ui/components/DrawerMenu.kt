package com.example.animeapp2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DrawerMenu() {
    // Definimos el degradado vertical "Vampírico" para un look Premium
    val drawerGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0D0D0D), // Negro puro arriba
            Color(0xFF240202)  // Rojo carmesí muy profundo abajo
        )
    )

    ModalDrawerSheet(
        drawerContainerColor = Color.Transparent,
        drawerContentColor = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(drawerGradient)
    ) {
        Spacer(Modifier.height(24.dp))

        Text(
            text = "Crimson List",
            style = MaterialTheme.typography.displayMedium.copy(),
            letterSpacing = 1.5.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }


}