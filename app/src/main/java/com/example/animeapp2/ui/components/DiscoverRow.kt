package com.example.animeapp2.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.animeapp2.data.model.AnimeManga

@Composable
fun DiscoverRow(
    title: String,
    animeList: List<AnimeManga>,
    onAnimeClick: (Int) -> Unit,
    onLoadMore: () -> Unit = {}
) {
    if (animeList.isNotEmpty()) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.primary
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(animeList) { index, anime ->
                    // Si llegamos cerca del final de la lista, disparamos la carga de más
                    if (index >= animeList.size - 5) {
                        onLoadMore()
                    }

                    AnimeMangaCard(
                        id = anime.id,
                        title = anime.title,
                        genres = anime.genres,
                        coverImage = anime.coverImage,
                        modifier = Modifier.width(160.dp),
                        onClick = { onAnimeClick(anime.id) }
                    )
                }
            }
        }
    }
}