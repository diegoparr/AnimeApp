package com.example.animeapp2.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.key.type
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.Optional
import com.example.animeapp2.GetAnimeMangasListByQuery
import com.example.animeapp2.GetAnimeMangasListQuery
import com.example.animeapp2.data.local.AppDatabase
import com.example.animeapp2.data.local.entities.TranslationEntity
import com.example.animeapp2.data.mapper.toDomain
import com.example.animeapp2.data.model.AnimeManga
import com.example.animeapp2.data.model.AnimeStatus
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

    var lastSearchQuery by mutableStateOf<String?>(null)
        private set

    var hasNextPage by mutableStateOf(true)
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

    fun fetchAnimes(isNewLoad: Boolean = false) {
        if (!hasNextPage && !isNewLoad) return

        viewModelScope.launch {
            if (isFetching) return@launch
            try {
                isFetching = true
                if (isNewLoad) {
                    currentPage = 1
                    animeMangaList = emptyList()
                    lastSearchQuery = null
                    hasNextPage = true
                }
                val response = apolloClient.query(GetAnimeMangasListQuery(page = Optional.present(currentPage))).execute()
                val remoteAnimeMangas = response.data?.Page?.media?.filterNotNull() ?: emptyList()
                
                if (remoteAnimeMangas.isEmpty()) {
                    hasNextPage = false
                } else {
                    animeMangaList += remoteAnimeMangas.map { it.toDomain() }
                    currentPage++
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isFetching = false
            }
        }
    }


    fun fetchAnimesByUserQuery(animeName: String, isNewSearch : Boolean = true){
        if (animeName.isBlank()) return
        if (!hasNextPage && !isNewSearch) return
        
        // Evitar buscar lo mismo si ya tenemos resultados
        if (isNewSearch && animeName == lastSearchQuery && animeMangaList.isNotEmpty()) return

        viewModelScope.launch {
            if (isFetching) return@launch
            try {
                isFetching = true

                if(isNewSearch) {
                    currentPage = 1
                    animeMangaList = emptyList()
                    lastSearchQuery = animeName
                    hasNextPage = true
                }
                val response = apolloClient.query(GetAnimeMangasListByQuery(
                    search= Optional.present(animeName),
                    page = Optional.present(currentPage))).execute()
                
                val remoteAnimeMangas = response.data?.Page?.media?.filterNotNull() ?: emptyList()
                
                if (remoteAnimeMangas.isEmpty()) {
                    hasNextPage = false
                } else {
                    animeMangaList += remoteAnimeMangas.map { it.toDomain() }
                    currentPage++
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isFetching = false
            }
        }
    }

    fun loadMore() {
        if (isFetching || !hasNextPage) return

        val query = lastSearchQuery
        if (query == null) {
            fetchAnimes()
        } else {
            fetchAnimesByUserQuery(query, isNewSearch = false)
        }
    }

    fun translateDescription(anime : AnimeManga) {

        val id = anime.id
        // 1. Si el usuario esta viendo la traduccion, al darle al boton de ver original se borra del estado

        if(_translations.containsKey(id)){
            _translations.remove(id)
            return
        }
        // 2. Si el usuario no esta viendo la traduccion, se recupera de la db o se traduce

        viewModelScope.launch{
            // Comprobacion de si ya esta traducida
            val existingTranslation = animeDao.getTranslation(anime.id)
            if(existingTranslation != null){
                _translations[anime.id] = existingTranslation // trae la traduccion de la db al estado
                return@launch
            }

            // Si no esta traducida, se traduce y se guarda en la db
            _translations[id] = "Traduciendo con IA..."
            val result = translator.translateText(anime.description.cleanHtml())
            _translations[id] = result

            // Tras traducir la sinopsis, se guarda en la db
            // Primero debe existir el respectivo AnimeManga en la db para que la traduccion exista
            animeDao.insertAnimeManga(
                com.example.animeapp2.data.local.entities.AnimeMangaEntity(
                    id_animemanga = anime.id,
                    titulo_romaji = anime.title.romaji,
                    titulo_english = anime.title.english,
                    titulo_native = anime.title.native,
                    descripcion = anime.description,
                    tipo = anime.type.name,
                    portada_url = anime.coverImage.large
                )
            )
            animeDao.insertTranslation(
                TranslationEntity(
                    id_animemanga = anime.id,
                    lenguaje = "es",
                    descripcion_traducida = result
                )
            )
        }
    }

    fun saveToLibrary(
        animeManga: AnimeManga,
        userId: Int,
        status: AnimeStatus,
        episodesWatched: Int,
        rating: Int?,
        isFavorite: Boolean

    ){

        viewModelScope.launch{
            try{
                // Primero debemos insertar el anime en la db
                animeDao.insertAnimeManga(com.example.animeapp2.data.local.entities.AnimeMangaEntity(
                    id_animemanga = animeManga.id,
                    titulo_romaji = animeManga.title.romaji,
                    titulo_english = animeManga.title.english,
                    titulo_native = animeManga.title.native,
                    descripcion = animeManga.description,
                    tipo = animeManga.type.name,
                    portada_url = animeManga.coverImage.large

                ))

                // Creamos el registro de la union entre el usuario y el anime

                val libraryEntry = com.example.animeapp2.data.local.entities.AnimeMangaUserCrossRef(
                    id_usuario = userId,
                    id_animemanga = animeManga.id,
                    estado = status,
                    episodios_vistos = episodesWatched,
                    nota_personal = rating,
                    es_favorito = isFavorite

                )
                // C. El DAO hace el insert en la tabla intermedia
                animeDao.insertToLibrary(libraryEntry)

            }catch ( e : Exception){
                e.printStackTrace()

            }
        }

    }

}
