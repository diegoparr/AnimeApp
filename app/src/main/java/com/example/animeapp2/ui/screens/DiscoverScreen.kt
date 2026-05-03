package com.example.animeapp2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.animeapp2.data.model.AnimeManga
import com.example.animeapp2.ui.components.CrimsonListTopBar
import com.example.animeapp2.ui.components.DiscoverRow
import com.example.animeapp2.ui.components.DrawerMenu
import com.example.animeapp2.ui.navigation.Screen
import com.example.animeapp2.viewmodel.AnimeMangaViewModel
import com.example.animeapp2.viewmodel.AuthUsersViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    animeMangaViewModel: AnimeMangaViewModel,
    authUsersViewModel: AuthUsersViewModel,
    navController: NavController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Cargar las secciones al entrar
    LaunchedEffect(Unit) {
        if (animeMangaViewModel.trendingList.isEmpty()) {
            animeMangaViewModel.loadDiscoverSections()
        }
    }

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
        Scaffold(
            topBar = { CrimsonListTopBar(drawerState = drawerState) },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                if (animeMangaViewModel.isFetching && animeMangaViewModel.trendingList.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 32.dp)
                    ) {
                        // 1. Hero Banner (Primer elemento de tendencias)
                        item {
                            animeMangaViewModel.trendingList.firstOrNull()?.let { hero ->
                                HeroBanner(anime = hero) {
                                    navController.navigate(Screen.Detail.createRoute(hero.id))
                                }
                            }
                        }

                        // 2. Filas de descubrimiento
                        item {
                            DiscoverRow(
                                title = "Tendencias 🔥",
                                animeList = animeMangaViewModel.trendingList,
                                onAnimeClick = { id -> navController.navigate(Screen.Detail.createRoute(id)) },
                                onLoadMore = { 
                                    animeMangaViewModel.loadNextPageForSection("trending", sort = listOf(com.example.animeapp2.type.MediaSort.TRENDING_DESC))
                                }
                            )
                        }

                        item {
                            DiscoverRow(
                                title = "Joyas Ocultas 💎",
                                animeList = animeMangaViewModel.hiddenGemsList,
                                onAnimeClick = { id -> navController.navigate(Screen.Detail.createRoute(id)) },
                                onLoadMore = {
                                    animeMangaViewModel.loadNextPageForSection("gems", popularityLesser = 50000, averageScoreGreater = 75)
                                }
                            )
                        }

                        item {
                            DiscoverRow(
                                title = "Más Esperados 🚄",
                                animeList = animeMangaViewModel.hypedUpcomingList,
                                onAnimeClick = { id -> navController.navigate(Screen.Detail.createRoute(id)) },
                                onLoadMore = {
                                    animeMangaViewModel.loadNextPageForSection("upcoming", status = com.example.animeapp2.type.MediaStatus.NOT_YET_RELEASED, sort = listOf(com.example.animeapp2.type.MediaSort.POPULARITY_DESC))
                                }
                            )
                        }

                        item {
                            DiscoverRow(
                                title = "Acción ⚔️",
                                animeList = animeMangaViewModel.actionList,
                                onAnimeClick = { id -> navController.navigate(Screen.Detail.createRoute(id)) },
                                onLoadMore = {
                                    animeMangaViewModel.loadNextPageForSection("action", genre = "Action", sort = listOf(com.example.animeapp2.type.MediaSort.POPULARITY_DESC))
                                }
                            )
                        }

                        item {
                            DiscoverRow(
                                title = "Romance 💖",
                                animeList = animeMangaViewModel.romanceList,
                                onAnimeClick = { id -> navController.navigate(Screen.Detail.createRoute(id)) },
                                onLoadMore = {
                                    animeMangaViewModel.loadNextPageForSection("romance", genre = "Romance", sort = listOf(com.example.animeapp2.type.MediaSort.POPULARITY_DESC))
                                }
                            )
                        }

                        item {
                            DiscoverRow(
                                title = "Aventura 🌍",
                                animeList = animeMangaViewModel.adventureList,
                                onAnimeClick = { id -> navController.navigate(Screen.Detail.createRoute(id)) },
                                onLoadMore = {
                                    animeMangaViewModel.loadNextPageForSection("adventure", genre = "Adventure", sort = listOf(com.example.animeapp2.type.MediaSort.POPULARITY_DESC))
                                }
                            )
                        }

                        item {
                            DiscoverRow(
                                title = "Horror 💀",
                                animeList = animeMangaViewModel.horrorList,
                                onAnimeClick = { id -> navController.navigate(Screen.Detail.createRoute(id)) },
                                onLoadMore = {
                                    animeMangaViewModel.loadNextPageForSection("horror", genre = "Horror", sort = listOf(com.example.animeapp2.type.MediaSort.POPULARITY_DESC))
                                }
                            )
                        }

                        item {
                            DiscoverRow(
                                title = "Thriller 🕵️",
                                animeList = animeMangaViewModel.thrillerList,
                                onAnimeClick = { id -> navController.navigate(Screen.Detail.createRoute(id)) },
                                onLoadMore = {
                                    animeMangaViewModel.loadNextPageForSection("thriller", genre = "Thriller", sort = listOf(com.example.animeapp2.type.MediaSort.POPULARITY_DESC))
                                }
                            )
                        }

                        item {
                            DiscoverRow(
                                title = "Psicológico 🧠",
                                animeList = animeMangaViewModel.psychologicalList,
                                onAnimeClick = { id -> navController.navigate(Screen.Detail.createRoute(id)) },
                                onLoadMore = {
                                    animeMangaViewModel.loadNextPageForSection("psychological", genre = "Psychological", sort = listOf(com.example.animeapp2.type.MediaSort.POPULARITY_DESC))
                                }
                            )
                        }

                        item {
                            DiscoverRow(
                                title = "Películas 🎬",
                                animeList = animeMangaViewModel.moviesList,
                                onAnimeClick = { id -> navController.navigate(Screen.Detail.createRoute(id)) },
                                onLoadMore = {
                                    animeMangaViewModel.loadNextPageForSection("movies", format = com.example.animeapp2.type.MediaFormat.MOVIE, sort = listOf(com.example.animeapp2.type.MediaSort.POPULARITY_DESC))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeroBanner(
    anime: AnimeManga,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = anime.coverImage.large,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradiente para legibilidad
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.5f),
                                Color.Black.copy(alpha = 0.9f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "DESTACADO HOY",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = anime.title.english ?: anime.title.romaji ?: "",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 28.sp
                    ),
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = anime.genres.take(3).joinToString(" • "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 1
                )
            }
        }
    }
}
