package com.example.animeapp2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.animeapp2.ui.components.*
import com.example.animeapp2.ui.navigation.Screen
import com.example.animeapp2.viewmodel.AnimeMangaViewModel
import com.example.animeapp2.viewmodel.AuthUsersViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLibraryScreen(
    animeMangaViewModel: AnimeMangaViewModel,
    authUsersViewModel: AuthUsersViewModel,
    navController: NavController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // 1. Sincronización del Usuario con la Tubería de la DB
    LaunchedEffect(authUsersViewModel.currentUser) {
        animeMangaViewModel.setUserId(authUsersViewModel.currentUser?.id_usuario)
    }

    // 2. Observamos la biblioteca filtrada en tiempo real
    val libraryItems by animeMangaViewModel.filteredLibrary.collectAsStateWithLifecycle()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenu(
                navController = navController,
                authviewModel = authUsersViewModel,
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = { CrimsonListTopBar(drawerState = drawerState) },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Título de la pantalla
                Text(
                    text = "MI BIBLIOTECA",
                    style = MaterialTheme.typography.displayMedium.copy(fontSize = 32.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                    fontWeight = FontWeight.Black
                )

                // 3. Barra de Filtros (Componente externo)
                LibraryFilterBar(
                    selectedFilter = animeMangaViewModel.selectedLibraryFilter,
                    onFilterSelected = { animeMangaViewModel.selectedLibraryFilter = it }
                )

                if (libraryItems.isEmpty()) {
                    // 4. Estado vacío (Componente externo)
                    EmptyLibraryState {
                        navController.navigate(Screen.Search.route)
                    }
                } else {
                    // 5. Cuadrícula de Animes
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(libraryItems) { item ->
                            // Tarjeta de item (Componente externo)
                            LibraryItemCard(item = item) {
                                navController.navigate(Screen.Detail.createRoute(item.libraryEntry.id_animemanga))
                            }
                        }
                    }
                }
            }
        }
    }
}
