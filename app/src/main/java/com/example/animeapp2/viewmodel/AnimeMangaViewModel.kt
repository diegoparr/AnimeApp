package com.example.animeapp2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.Optional
import com.example.animeapp2.GetAnimeMangasListQuery
import com.example.animeapp2.data.mapper.toDomain
import com.example.animeapp2.data.model.AnimeManga
import com.example.animeapp2.data.network.apolloClient
import com.example.animeapp2.util.TranslatorManager
import kotlinx.coroutines.launch

class AnimeMangaViewModel : ViewModel() {
    private val translator = TranslatorManager()

    var animeMangaList by mutableStateOf<List<AnimeManga>>(emptyList())
    private set

    var currentPage = 1
    private set

    var isFetching by mutableStateOf(false)
        private set

    // Mapa reactivo para guardar las traducciones por ID de anime
    private val _translations = mutableStateMapOf<Int, String>()

    fun getTranslationFor(id: Int): String = _translations[id] ?: ""

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

    fun translateDescription(id: Int, text: String) {
        // Si ya está traduciendo o ya existe, evitamos repetir
        if (_translations[id] == "Traduciendo con IA...") return
        
        viewModelScope.launch {
            _translations[id] = "Traduciendo con IA..."
            val result = translator.translateText(text)
            _translations[id] = result
        }
    }

    fun clearTranslation(id: Int) {
        _translations.remove(id)
    }
}
