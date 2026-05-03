package com.example.animeapp2.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.animeapp2.data.local.entities.AnimeMangaUserCrossRef
import com.example.animeapp2.data.model.AnimeManga
import com.example.animeapp2.data.model.AnimeStatus
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToLibrarySheet(
    anime: AnimeManga,
    currentEntry: AnimeMangaUserCrossRef?,
    accentColor: Color,
    onDismiss: () -> Unit,
    onSave: (status: AnimeStatus, episodes: Int, rating: Int?, isFav: Boolean) -> Unit
) {
    // Estados locales para manejar la UI antes de guardar
    var selectedStatus by remember { mutableStateOf(currentEntry?.estado ?: AnimeStatus.VIENDO) }
    var episodesWatched by remember { mutableStateOf(currentEntry?.episodios_vistos ?: 0) }
    var personalRating by remember { mutableStateOf(currentEntry?.nota_personal?.toFloat() ?: 0f) }
    var isFavorite by remember { mutableStateOf(currentEntry?.es_favorito ?: false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color.White.copy(alpha = 0.2f)) },
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 48.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Cabecera (Mini Info)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = anime.coverImage.large,
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp, 90.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = anime.title.romaji,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        color = Color.White
                    )
                    Text(
                        text = anime.type.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor
                    )
                }

                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (isFavorite) accentColor else Color.White.copy(alpha = 0.4f)
                    )
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Selector de Estado
            Text(
                text = "ESTADO",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = 0.6f),
                letterSpacing = 2.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            @OptIn(ExperimentalLayoutApi::class)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                AnimeStatus.entries.forEach { status ->
                    val isSelected = selectedStatus == status
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedStatus = status },
                        label = { Text(status.name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = accentColor.copy(alpha = 0.2f),
                            selectedLabelColor = accentColor,
                            labelColor = Color.White.copy(alpha = 0.6f)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = Color.White.copy(alpha = 0.1f),
                            selectedBorderColor = accentColor,
                            borderWidth = 1.dp,
                            selectedBorderWidth = 1.dp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Progreso de Episodios
            Text(
                text = "EPISODIOS VISTOS",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = 0.6f),
                letterSpacing = 2.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { if (episodesWatched > 0) episodesWatched-- },
                    modifier = Modifier.background(Color.White.copy(alpha = 0.05f), CircleShape)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = null, tint = Color.White)
                }
                
                Text(
                    text = episodesWatched.toString(),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 32.dp),
                    color = Color.White
                )

                IconButton(
                    onClick = { episodesWatched++ },
                    modifier = Modifier.background(accentColor.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = accentColor)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Nota Personal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TU NOTA",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.6f),
                    letterSpacing = 2.sp
                )
                Text(
                    text = if (personalRating > 0) personalRating.roundToInt().toString() else "--",
                    style = MaterialTheme.typography.titleLarge,
                    color = accentColor,
                    fontWeight = FontWeight.Black
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Slider(
                value = personalRating,
                onValueChange = { personalRating = it },
                valueRange = 0f..100f,
                steps = 100,
                colors = SliderDefaults.colors(
                    thumbColor = accentColor,
                    activeTrackColor = accentColor,
                    inactiveTrackColor = Color.White.copy(alpha = 0.1f)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 5. Botón de Guardar
            Button(
                onClick = { 
                    onSave(
                        selectedStatus, 
                        episodesWatched, 
                        if (personalRating > 0) personalRating.roundToInt() else null, 
                        isFavorite
                    ) 
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "GUARDAR EN MI LISTA",
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
