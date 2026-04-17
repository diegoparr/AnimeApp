package com.example.animeapp2.ui.screens

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.animeapp2.data.model.AnimeManga
import com.example.animeapp2.viewmodel.AnimeMangaViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnimeMangaDetailScreen(
    anime: AnimeManga,
    viewModel: AnimeMangaViewModel,
    onBackClick: () -> Unit
) {
    // Obtenemos la traducción específica para este anime desde el mapa del ViewModel
    val translation = viewModel.getTranslationFor(anime.id)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // 1. Cabecera con Imagen y degradado inmersivo
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(450.dp)
                    ) {
                        AsyncImage(
                            model = anime.coverImage.large,
                            contentDescription = anime.title.romaji,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                                            MaterialTheme.colorScheme.background
                                        ),
                                        startY = 400f
                                    )
                                )
                        )
                    }
                }

                // 2. Título y Etiquetas (Chips)
                item {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 0.dp)
                    ) {
                        Text(
                            text = (anime.title.english ?: anime.title.romaji).uppercase(),
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.primary,
                            lineHeight = 34.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            SuggestionChip(
                                onClick = { },
                                label = { Text(anime.type) },
                                shape = RoundedCornerShape(8.dp),
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    labelColor = MaterialTheme.colorScheme.primary
                                )
                            )

                            anime.genres.forEach { genre ->
                                SuggestionChip(
                                    onClick = { },
                                    label = { Text(genre) },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        labelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                )
                            }
                        }
                    }
                }

                // 3. Sección de Sinopsis con TRADUCCIÓN (Basada en ID)
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
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 2.sp,
                                fontWeight = FontWeight.Bold
                            )
                            
                            // BOTÓN DE TRADUCCIÓN: Habla con el Mapa del ViewModel usando el ID
                            TextButton(
                                onClick = { 
                                    if (translation.isEmpty()) {
                                        viewModel.translateDescription(anime.id, anime.description.cleanHtmlTags())
                                    } else {
                                        viewModel.clearTranslation(anime.id)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Translate, 
                                    contentDescription = null, 
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = if (translation.isEmpty()) "Traducir" else "Ver original",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Lógica de visualización corregida para usar la traducción del mapa
                        val descriptionToShow = when {
                            translation == "Traduciendo con IA..." -> "Procesando sinopsis con Gemini AI..."
                            translation.isNotEmpty() -> translation
                            else -> anime.description.cleanHtmlTags()
                        }

                        Text(
                            text = descriptionToShow,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                            lineHeight = 22.sp
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }

            // 4. Botón de Acción Flotante
            Button(
                onClick = { /* Lógica futura */ },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.Favorite, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "AÑADIR A MI LISTA",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // 5. Botón de Atrás
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    .size(40.dp),
                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver"
                )
            }
        }
    }
}

fun String.cleanHtmlTags(): String {
    return this.replace(Regex("<[^>]*>"), "")
}
