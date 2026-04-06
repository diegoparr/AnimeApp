package com.example.animeapp2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.animeapp2.data.model.CoverImage
import com.example.animeapp2.data.model.Title
import androidx.core.graphics.toColorInt

@Composable
fun AnimeMangaCard(
    id: Int,
    title: Title,
    genres: List<String>,
    coverImage: CoverImage,
    modifier: Modifier = Modifier
) {
    // Intentamos parsear el color hexadecimal que viene de la API
    val apiBackgroundColor = try {
        coverImage.color?.let { Color(it.toColorInt()) } ?: Color(0xFF1A1A1A)
    } catch (e: Exception) {
        Color(0xFF1A1A1A) // Color oscuro por defecto si falla el parseo
    }

    Card(
        modifier = modifier
            .padding(6.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = apiBackgroundColor)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Imagen de fondo traida de API, con recorte suave (Relación 2:3 estilo poster)
            AsyncImage(
                model = coverImage.large,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f),
                contentScale = ContentScale.Crop
            )

            // Badge superior (Género/Tipo) - Estilo sutil "Pill"
            if (genres.isNotEmpty()) {
                Surface(
                    color = Color(0xFFD50B48),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = genres[0].uppercase(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Degradado inferior alto para asegurar legibilidad del texto blanco
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f),
                                Color.Black.copy(alpha = 0.9f)
                            )
                        )
                    )
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 24.dp)
            ) {
                Text(
                    text = title.english ?: title.romaji ?: title.native, // Si no hay titulo en ingles, usar romaji o native
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
fun AnimeMangaCardPreview() {
    AnimeMangaCard(
        id = 20,
        title = Title("Naruto", "Naruto", "Naruto"),
        genres = listOf("Action","Adventure","Comedy","Drama","Fantasy","Supernatural"),
        coverImage = CoverImage(large = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx20-dE6UHbFFg1A5.jpg", color = "e47850"))
}
