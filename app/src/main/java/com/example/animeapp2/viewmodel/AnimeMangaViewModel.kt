package com.example.animeapp2.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.Optional
import com.example.animeapp2.GetAnimeMangasListQuery
import com.example.animeapp2.data.local.AppDatabase
import com.example.animeapp2.data.local.entities.TranslationEntity
import com.example.animeapp2.data.mapper.toDomain
import com.example.animeapp2.data.model.AnimeManga
import com.example.animeapp2.data.network.apolloClient
import com.example.animeapp2.util.TranslatorManager
import com.example.animeapp2.util.cleanHtml
import kotlinx.coroutines.launch

class AnimeMangaViewModel(application: Application) : AndroidViewModel(application) {
    private val translator = TranslatorManager()
    private val animeDao = AppDatabase.getDatabase(application).animeMangaDao()

    var animeMangaList by mutableStateOf<List<AnimeManga>>(emptyList())
    private set

    var currentPage = 1
    private set

    var isFetching by mutableStateOf(false)
        private set

    // Mapa para mantener la UI reactiva mientras cargamos
    private val _translations = mutableStateMapOf<Int, String>()

    fun getTranslationFor(id: Int): String = _translations[id] ?: ""

    // Carga la traducción desde la DB al entrar a la pantalla de detalle
    fun loadTranslationFromDb(animeId: Int) {
        viewModelScope.launch {
            val cached = animeDao.getTranslation(animeId)
            if (cached != null) {
                _translations[animeId] = cached
            }
        }
    }

    fun fetchAnimes() {
        viewModelScope.launch {
            if (isFetching) return@launch
            try {
                isFetching = true
                val response = apolloClient.query(GetAnimeMangasListQuery(page = Optional.present(currentPage))).execute()
                val remoteAnimeMangas = response.data?.Page?.media?.filterNotNull() ?: emptyList()
                animeMangaList += remoteAnimeMangas.map { it.toDomain() }
                currentPage++
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isFetching = false
            }
        }
    }

    fun translateDescription(anime : AnimeManga) {
        if (_translations[anime.id] == "Traduciendo con IA...") return
        viewModelScope.launch{
            animeDao.insertAnimeManga(
                com.example.animeapp2.data.local.entities.AnimeMangaEntity(
                    id_animemanga = anime.id,
                    titulo_romaji = anime.title.romaji,
                    titulo_english = anime.title.english,
                    titulo_native = anime.title.native,
                    descripcion = anime.description,
                    tipo = anime.type,
                    portada_url = anime.coverImage.large
                )
            )
            _translations[anime.id] = "Traduciendo con IA..."
            val result = translator.translateText(anime.description.cleanHtml())

            _translations[anime.id] = result

            animeDao.insertTranslation(
                TranslationEntity(
                    id_animemanga = anime.id,
                    lenguaje = "es",
                    descripcion_traducida = result
                )
            )
        }
    }

    fun clearTranslation(id: Int) {
        // En persistencia no solemos borrar el mapa, 
        // pero podemos resetear el estado visual si es necesario.
    }
}
