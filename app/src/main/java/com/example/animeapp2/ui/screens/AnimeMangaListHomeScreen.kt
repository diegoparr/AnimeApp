package com.example.animeapp2.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.animeapp2.ui.components.AnimeMangaCard
import com.example.animeapp2.ui.components.CrimsonListTopBar
import com.example.animeapp2.ui.components.DrawerMenu
import com.example.animeapp2.viewmodel.AnimeMangaViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel : AnimeMangaViewModel = viewModel(),
    onAnimeClick : (Int) -> Unit,
) {

    LaunchedEffect(Unit) {
        viewModel.fetchAnimes()
    }


    // Inicializa el estado de la barra lateral
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenu()
        }
    ) {

        val animeMangaList = viewModel.animeMangaList
        // 1. EL ANCLA DEL THEME: El Surface principal
        Surface(
            modifier = Modifier.fillMaxSize(),
            // Se toma el color definido en Theme.kt
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 1.dp
        ) {
            // 2. LA ESTRUCTURA: El Scaffold organiza el contenido
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    CrimsonListTopBar(drawerState = drawerState)
                }
            ) { innerPadding ->
                // 3. EL CONTENIDO: Grilla de 3 columnas
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    itemsIndexed(animeMangaList) { index, animeManga ->

                        if(index >= animeMangaList.size - 6 && !viewModel.isFetching){
                            LaunchedEffect(animeMangaList.size) {
                                viewModel.fetchAnimes()
                            }
                        }
                        AnimeMangaCard(
                            id = animeManga.id,
                            title = animeManga.title,
                            genres = animeManga.genres,
                            coverImage = animeManga.coverImage,
                            onClick = {onAnimeClick(animeManga.id)}
                        )
                    }
                }
            }
        }
    }
}