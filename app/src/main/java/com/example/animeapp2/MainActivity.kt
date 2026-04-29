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
import com.example.animeapp2.ui.screens.LoginScreen
import com.example.animeapp2.ui.screens.RegisterScreen
import com.example.animeapp2.ui.theme.AnimeApp2Theme
import com.example.animeapp2.viewmodel.AnimeMangaViewModel
import com.example.animeapp2.viewmodel.AuthUsersViewModel

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
                    val animeMangaViewModel: AnimeMangaViewModel = viewModel()
                    val authUsersViewModel : AuthUsersViewModel = viewModel()

                    val startRoute = if (authUsersViewModel.currentUser != null) {
                        Screen.Home.route
                    } else {
                        Screen.Login.route
                    }

                    NavHost(
                        navController = navController,
                        startDestination = startRoute
                    ) {
                        // RUTA 1: Pantalla de Login
                        composable(Screen.Login.route) {
                            LoginScreen(
                                viewModel = authUsersViewModel,
                                onLoginSuccess = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                    }
                                },
                                onLoginError = {
                                    // Manejar el error de inicio de sesión aquí
                                },
                                onRegisterClick = {
                                    navController.navigate(Screen.Register.route)
                                }
                            )
                        }

                        // RUTA 2: Pantalla de Registro
                        composable(Screen.Register.route) {
                            RegisterScreen(
                                viewModel = authUsersViewModel,
                                onRegisterSuccess = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                    }
                                },
                                onLoginClick = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // RUTA 3: Pantalla Principal (Lista)
                        composable(Screen.Home.route) {
                            HomeScreen(
                                animeMangaviewModel = animeMangaViewModel,
                                authUsersViewModel = authUsersViewModel,
                                navController = navController,
                                onAnimeClick = { id ->
                                    navController.navigate(Screen.Detail.createRoute(id))
                                }
                            )
                        }

                        // RUTA 4: Pantalla de Detalle (Recibe el ID)
                        composable(
                            route = Screen.Detail.route,
                            arguments = listOf(navArgument("animeId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val animeId = backStackEntry.arguments?.getInt("animeId") ?: 0
                            
                            // Buscamos el anime específico en la lista que el ViewModel ya tiene
                            val selectedAnime = animeMangaViewModel.animeMangaList.find { it.id == animeId }
                            
                            if (selectedAnime != null) {
                                AnimeMangaDetailScreen(
                                    anime = selectedAnime,
                                    viewModel = animeMangaViewModel, // Pasamos el viewModel para la traducción
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
