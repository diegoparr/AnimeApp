package com.example.animeapp2.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import coil3.compose.AsyncImage
import com.example.animeapp2.data.model.AnimeManga
import com.example.animeapp2.ui.components.AddToLibrarySheet
import com.example.animeapp2.viewmodel.AnimeMangaViewModel
import com.example.animeapp2.viewmodel.AuthUsersViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnimeMangaDetailScreen(
    anime: AnimeManga,
    viewModel: AnimeMangaViewModel,
    authViewModel: AuthUsersViewModel,
    onBackClick: () -> Unit,
) {
    var showSheet by remember { mutableStateOf(false) }
    val currentUser = authViewModel.currentUser

    // Definimos el color de acento fuera de bloques conflictivos
    val accentColor = remember(anime.coverImage.color) {
        try {
            anime.coverImage.color?.let { Color(it.toColorInt()) } ?: Color(0xFFD50B48)
        } catch (_: Exception) {
            Color(0xFFD50B48)
        }
    }

    LaunchedEffect(anime.id, currentUser?.id_usuario) {
        viewModel.loadTranslationFromDb(anime.id)
        currentUser?.id_usuario?.let { userId ->
            viewModel.loadLibraryEntry(userId, anime.id)
        }
    }

    val translation = viewModel.getTranslationFor(anime.id)
    val libraryEntry = viewModel.currentLibraryEntry

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // 1. Cabecera con Imagen, degradado y Score Badge
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(480.dp)
                    ) {
                        AsyncImage(
                            model = anime.coverImage.large,
                            contentDescription = anime.title.romaji,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        
                        // Degradado inmersivo más profundo
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            MaterialTheme.colorScheme.background.copy(alpha = 0.4f),
                                            MaterialTheme.colorScheme.background
                                        ),
                                        startY = 300f
                                    )
                                )
                        )

                        // Badge de Score (Puntaje) - Estética mejorada
                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(20.dp),
                            color = Color.Black.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "${anime.averageScore ?: "--"}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // 2. Título y Etiquetas (Chips)
                item {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = (anime.title.english ?: anime.title.romaji).uppercase(),
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-1).sp
                            ),
                            color = Color.White,
                            lineHeight = 36.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Badge de Tipo con color de acento
                            Surface(
                                color = accentColor.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, accentColor.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = anime.type.name,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            anime.genres.forEach { genre ->
                                SuggestionChip(
                                    onClick = { },
                                    label = { Text(genre, color = Color.White) },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                                        labelColor = Color.White
                                    ),
                                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                                )
                            }
                        }
                    }
                }

                // 3. Sección de Sinopsis
                item {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SINOPSIS",
                                style = MaterialTheme.typography.labelLarge,
                                color = accentColor,
                                letterSpacing = 2.sp,
                                fontWeight = FontWeight.Black
                            )
                            
                            TextButton(
                                onClick = { viewModel.translateDescription(anime) },
                                colors = ButtonDefaults.textButtonColors(contentColor = accentColor)
                            ) {
                                Icon(Icons.Default.Translate, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = if (translation.isEmpty()) "Traducir" else "Ver original",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        val descriptionToShow = when {
                            translation == "Traduciendo con IA..." -> "Procesando sinopsis con Gemini AI..."
                            translation.isNotEmpty() -> translation
                            else -> anime.description.cleanHtmlTags()
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = descriptionToShow,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White.copy(alpha = 0.9f),
                                lineHeight = 24.sp
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(120.dp)) }
            }

            // 4. Botón de Acción con efecto Glow
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
            ) {
                Button(
                    onClick = { showSheet = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(16.dp),
                            ambientColor = accentColor,
                            spotColor = accentColor
                        ),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Icon(
                        imageVector = if (libraryEntry != null) Icons.Default.Star else Icons.Default.Favorite, 
                        contentDescription = null, 
                        tint = Color.White
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = if (libraryEntry != null) "EN TU LISTA" else "AÑADIR A MI LISTA",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }
            }

            // 5. Botón de Atrás
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    .size(44.dp),
                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }

            // 6. Hoja de añadir a la lista
            if (showSheet) {
                AddToLibrarySheet(
                    anime = anime,
                    currentEntry = libraryEntry,
                    accentColor = accentColor,
                    onDismiss = { showSheet = false },
                    onSave = { status, episodes, rating, isFav ->
                        currentUser?.id_usuario?.let { userId ->
                            viewModel.saveToLibrary(
                                animeManga = anime,
                                userId = userId,
                                status = status,
                                episodesWatched = episodes,
                                rating = rating,
                                isFavorite = isFav
                            )
                        }
                        showSheet = false
                    }
                )
            }
        }
    }
}

fun String.cleanHtmlTags(): String {
    return this.replace(Regex("<[^>]*>"), "")
}
