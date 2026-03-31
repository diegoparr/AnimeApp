package com.example.animeapp2.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.animeapp2.data.model.AnimeManga
import androidx.compose.foundation.lazy.grid.items
import com.example.animeapp2.R
import com.example.animeapp2.ui.components.AnimeMangaCard
import com.example.animeapp2.ui.components.CrimsonListTopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(mangaList: List<AnimeManga>) {
    // 1. EL ANCLA DEL THEME: El Surface principal
    Surface(
        modifier = Modifier.fillMaxSize(),
        // Aquí es donde ocurre la magia: toma el color que definiste en Theme.kt
        color = MaterialTheme.colorScheme.background,
        // Tip de pro: la elevación tonal le da un brillo sutil muy elegante
        tonalElevation = 1.dp
    ) {
        // 2. LA ESTRUCTURA: El Scaffold organiza el contenido
        Scaffold(
            containerColor = Color.Transparent, // Dejamos que se vea el color del Surface
            topBar = {
                CrimsonListTopBar()
            }
        ) { innerPadding ->
            // 3. EL CONTENIDO: Tu grilla de 3 columnas
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(mangaList) { manga ->
                    AnimeMangaCard(
                        id = manga.id,
                        title = manga.title,
                        genres = manga.genres,
                        coverImage = manga.coverImage,
                        modifier = Modifier.padding(1 .dp)
                    )
                }
            }
        }
    }
}