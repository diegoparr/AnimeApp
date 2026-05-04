package com.example.animeapp2.ui.screens

import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.animeapp2.ui.components.AnimeMangaCard
import com.example.animeapp2.ui.components.CrimsonListTopBar
import com.example.animeapp2.ui.components.DrawerMenu
import com.example.animeapp2.ui.components.SearchBar
import com.example.animeapp2.viewmodel.AnimeMangaViewModel
import com.example.animeapp2.viewmodel.AuthUsersViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    animeMangaviewModel : AnimeMangaViewModel = viewModel(),
    authUsersViewModel : AuthUsersViewModel = viewModel(),
    navController : NavHostController,
    onAnimeClick : (Int) -> Unit,
) {

    LaunchedEffect(Unit) {
        animeMangaviewModel.fetchAnimes()
    }


    // Inicializa el estado de la barra lateral
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenu(
                navController = navController,
                authviewModel = authUsersViewModel,
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {

        val animeMangaList = animeMangaviewModel.animeMangaDefaultList
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

                Column(modifier = Modifier.padding(innerPadding)) {

                    SearchBar(
                        searchQuery = animeMangaviewModel.searchText,
                        onSearchQueryChange = { animeMangaviewModel.searchText = it },
                        onSearch = { query ->
                            animeMangaviewModel.fetchAnimesByUserQuery(query)
                        },
                        onClear = {
                            animeMangaviewModel.fetchAnimes(isNewLoad = true)
                        },
                        isSearchActive = animeMangaviewModel.isSearchActive,
                        onIsSearchActiveChange = { animeMangaviewModel.isSearchActive = it }
                    )

                    // 3. EL CONTENIDO: Grilla de 3 columnas
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        itemsIndexed(animeMangaList) { index, animeManga ->

                            if(index >= animeMangaList.size - 6 && !animeMangaviewModel.isFetching && animeMangaviewModel.hasNextPage){
                                LaunchedEffect(animeMangaList.size) {
                                    animeMangaviewModel.loadMore()
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
}