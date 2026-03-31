package com.example.animeapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.animeapp2.data.model.AnimeManga
import com.example.animeapp2.data.model.CoverImage
import com.example.animeapp2.data.model.Title
import com.example.animeapp2.ui.screens.HomeScreen
import com.example.animeapp2.ui.theme.AnimeApp2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. DATOS DE PRUEBA (Mock Data)
        // Usamos IDs de prueba y links de imágenes reales de AniList para testear
        val mockMangaList = listOf(
            AnimeManga(
                id = 1,
                title = Title("Naruto", "Naruto Shippuden", "ナルト- 疾風伝"),
                type = "ANIME",
                genres = listOf("Action", "Adventure"),
                description = "Naruto Uzumaki wants to be the best ninja...",
                coverImage = CoverImage("https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx20-dE6UHbFFg1A5.jpg")
            ),
            AnimeManga(
                id = 2,
                title = Title("One Piece", "One Piece", "ONE PIECE"),
                type = "ANIME",
                genres = listOf("Action", "Adventure", "Comedy"),
                description = "Gol D. Roger was known as the 'Pirate King'...",
                coverImage = CoverImage("https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx21-ELSYx3yMPcKM.jpg")
            ),
            AnimeManga(
                id = 3,
                title = Title("Bleach", "Bleach", "BLEACH"),
                type = "ANIME",
                genres = listOf("Action", "Adventure", "Supernatural"),
                description = "Ichigo Kurosaki is a teenager gifted with the ability to see spirits...",
                coverImage = CoverImage("https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx269-d2GmRkJbMopq.png")
            ),
            AnimeManga(
                id = 4,
                title = Title("Attack on Titan", "Shingeki No Kyojin", "進撃の巨人"),
                type = "ANIME",
                genres = listOf("Action", "Drama", "Fantasy", "Mystery"),
                description = "Centuries ago, mankind was slaughtered to near extinction by monstrous humanoid creatures called Titans...",
                coverImage = CoverImage("https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx16498-buvcRTBx4NSm.jpg")
            ),
            AnimeManga(
                id = 5,
                title = Title("Jujutsu Kaisen", "Jujutsu Kaisen", "呪術廻戦"),
                type = "ANIME",
                genres = listOf("Action", "Supernatural"),
                description = "A boy fights... for the right death.",
                coverImage = CoverImage("https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx113415-LHBAeoZDIsnF.jpg")
            ),
            AnimeManga(
                id = 6,
                title = Title("Demon Slayer: Kimetsu no Yaiba", "Kimetsu no Yaiba", "鬼滅の刃"),
                type = "ANIME",
                genres = listOf("Action", "Adventure", "Drama", "Supernatural"),
                description = "It is the Taisho Period in Japan...",
                coverImage = CoverImage("https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx112151-1qlQwPB1RrJe.png")
            )
        )

        setContent {
            // 2. EL ENVOLTORIO: Tu tema personalizado
            AnimeApp2Theme(darkTheme = true) {
                // 3. EL LIENZO: Surface para el fondo
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 4. LA PANTALLA: Llamamos a tu HomeScreen
                    HomeScreen(mangaList = mockMangaList)
                }
            }
        }
    }
}
