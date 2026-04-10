package com.example.animeapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.animeapp2.data.model.AnimeManga
import com.example.animeapp2.data.model.CoverImage
import com.example.animeapp2.data.model.Title
import com.example.animeapp2.ui.screens.HomeScreen
import com.example.animeapp2.ui.theme.AnimeApp2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 2. EL ENVOLTORIO: tema personalizado
            AnimeApp2Theme(darkTheme = true) {
                // 3. EL LIENZO: Surface para el fondo
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 4. LA PANTALLA: Llamamos al HomeScreen
                    HomeScreen()
                }
            }
        }
    }
}
