package com.example.animeapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.animeapp2.ui.navigation.Screen
import com.example.animeapp2.ui.screens.AnimeMangaDetailScreen
import com.example.animeapp2.ui.screens.HomeScreen
import com.example.animeapp2.ui.theme.AnimeApp2Theme
import com.example.animeapp2.viewmodel.AnimeMangaViewModel

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
                    val navController = rememberNavController()
                    // Obtenemos el ViewModel aquí para compartir la lista entre pantallas si es necesario
                    val viewModel: AnimeMangaViewModel = viewModel()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route
                    ) {
                        // RUTA 1: Pantalla Principal (Lista)
                        composable(Screen.Home.route) {
                            HomeScreen(
                                viewModel = viewModel,
                                onAnimeClick = { id ->
                                    navController.navigate(Screen.Detail.createRoute(id))
                                }
                            )
                        }

                        // RUTA 2: Pantalla de Detalle (Recibe el ID)
                        composable(
                            route = Screen.Detail.route,
                            arguments = listOf(navArgument("animeId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val animeId = backStackEntry.arguments?.getInt("animeId") ?: 0
                            
                            // Buscamos el anime específico en la lista que el ViewModel ya tiene
                            val selectedAnime = viewModel.animeMangaList.find { it.id == animeId }
                            
                            if (selectedAnime != null) {
                                AnimeMangaDetailScreen(
                                    anime = selectedAnime,
                                    viewModel = viewModel, // Pasamos el viewModel para la traducción
                                    onBackClick = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
