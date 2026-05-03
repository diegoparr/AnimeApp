package com.example.animeapp2.viewmodel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.input.key.type
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.Optional
import com.example.animeapp2.GetAnimeMangasListByQuery
import com.example.animeapp2.GetAnimeMangasListQuery
import com.example.animeapp2.GetDiscoverAnimeMangasQuery
import com.example.animeapp2.data.local.AppDatabase
import com.example.animeapp2.data.local.entities.TranslationEntity
import com.example.animeapp2.data.mapper.toDomain
import com.example.animeapp2.data.model.AnimeManga
import com.example.animeapp2.data.model.AnimeStatus
import com.example.animeapp2.data.network.apolloClient
import com.example.animeapp2.type.MediaFormat
import com.example.animeapp2.type.MediaSeason
import com.example.animeapp2.type.MediaSort
import com.example.animeapp2.type.MediaStatus
import com.example.animeapp2.util.TranslatorManager
import com.example.animeapp2.util.cleanHtml
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AnimeMangaViewModel(application: Application) : AndroidViewModel(application) {
    private val translator = TranslatorManager()
    private val animeDao = AppDatabase.getDatabase(application).animeMangaDao()

    var animeMangaDefaultList by mutableStateOf<List<AnimeManga>>(emptyList())
        private set

    // Listas separadas para cada fila de la pantalla Discover
    var trendingList by mutableStateOf<List<AnimeManga>>(emptyList())
    var actionList by mutableStateOf<List<AnimeManga>>(emptyList())
    var romanceList by mutableStateOf<List<AnimeManga>>(emptyList())
    var adventureList by mutableStateOf<List<AnimeManga>>(emptyList())
    var horrorList by mutableStateOf<List<AnimeManga>>(emptyList())
    var thrillerList by mutableStateOf<List<AnimeManga>>(emptyList())
    var psychologicalList by mutableStateOf<List<AnimeManga>>(emptyList())
    var hypedUpcomingList by mutableStateOf<List<AnimeManga>>(emptyList())
    var hiddenGemsList by mutableStateOf<List<AnimeManga>>(emptyList())

    var moviesList by mutableStateOf<List<AnimeManga>>(emptyList())

    var currentPage = 1
        private set

    var isFetching by mutableStateOf(false)
        private set

    var lastSearchQuery by mutableStateOf<String?>(null)
        private set

    var hasNextPage by mutableStateOf(true)
        private set

    // Estado para la entrada actual en la biblioteca del usuario
    var currentLibraryEntry by mutableStateOf<com.example.animeapp2.data.local.entities.AnimeMangaUserCrossRef?>(
        null
    )
        private set

    private val _sectionsState = mutableStateMapOf<String, SectionState>()

    fun getListForSection(section: String) = _sectionsState[section]?.list ?: emptyList()
    fun isSectionFetching(section: String) = _sectionsState[section]?.isFetching ?: false


    // Estado sincronizado con la DB para identificar id de usuario
    private val _currentUserId = MutableStateFlow<Int?>(null)

    fun setUserId(userId: Int?) {
        // Esta funcion se llamara desde la UI al abrir la biblioteca
        _currentUserId.value = userId
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _dbLibraryFlow = _currentUserId.flatMapLatest { userId ->
        if (userId == null) flowOf(emptyList()) // Si no hay usuario, lista vacía
        else animeDao.getUserLibrary(userId)   // Si hay, se abre el grifo de Room
    }

    // Variable de estado para identificar como quiso filtrar su libreria el usuario
    var selectedLibraryFilter by mutableStateOf<AnimeStatus?>(null)

    /**
     * filteredLibrary es la lista reactiva que vera el usuario en la pantalla de su biblioteca
     * combine observa varios flows a la vez, en este caso observa el flow que consulta la libreria del
     * usuario constantemente, y el estado del filtro que selecciono el usuario para filtrar
     * su biblioteca
      */

    val filteredLibrary = combine(
        _dbLibraryFlow,
        snapshotFlow { selectedLibraryFilter } // Convertimos el filtro en flujo de datos
    ) { list, filter ->       // Asignamos el flow a las dos variables temporales respectivamente
        if (filter == null) list
        else list.filter { it.libraryEntry.estado == filter }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // Optimización de batería
        initialValue = emptyList()
    )

    // Gestión de paginación para secciones de Discover
    data class SectionState(
        val list: List<AnimeManga> = emptyList(),
        val currentPage: Int = 1,
        val hasNextPage: Boolean = true,
        val isFetching: Boolean = false
    )


    // Mapa para mantener la UI reactiva mientras cargamos
    private val _translations = mutableStateMapOf<Int, String>()



    fun getTranslationFor(id: Int): String = _translations[id] ?: ""

    // Carga la información de la biblioteca para el usuario y anime actual
    fun loadLibraryEntry(userId: Int, animeId: Int) {
        viewModelScope.launch {
            currentLibraryEntry = animeDao.getUserLibraryEntry(userId, animeId)
        }
    }

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
                    animeMangaDefaultList = emptyList()
                    lastSearchQuery = null
                    hasNextPage = true
                }
                val response = apolloClient.query(GetAnimeMangasListQuery(page = Optional.present(currentPage))).execute()
                val remoteAnimeMangas = response.data?.Page?.media?.filterNotNull() ?: emptyList()
                
                if (remoteAnimeMangas.isEmpty()) {
                    hasNextPage = false
                } else {
                    animeMangaDefaultList += remoteAnimeMangas.map { it.toDomain() }
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
        if (isNewSearch && animeName == lastSearchQuery && animeMangaDefaultList.isNotEmpty()) return

        viewModelScope.launch {
            if (isFetching) return@launch
            try {
                isFetching = true

                if(isNewSearch) {
                    currentPage = 1
                    animeMangaDefaultList = emptyList()
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
                    animeMangaDefaultList += remoteAnimeMangas.map { it.toDomain() }
                    currentPage++
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isFetching = false
            }
        }
    }


    private suspend fun fetchDiscoverAnimes(
        genre: String? = null,
        sort: List<MediaSort>? = null,
        season: MediaSeason? = null,
        seasonYear: Int? = null,
        popularityLesser : Int? =  null,
        averageScoreGreater : Int? = null,
        format: MediaFormat? = null,
        status : MediaStatus? = null,
        page: Int = 1
    ): List<AnimeManga> {
        return try {
            // Llamada a la query con los parámetros opcionales
            val response = apolloClient.query(
                GetDiscoverAnimeMangasQuery(
                    page = Optional.present(page),
                    genre = Optional.presentIfNotNull(genre),
                    sort = Optional.presentIfNotNull(sort),
                    season = Optional.presentIfNotNull(season),
                    seasonYear = Optional.presentIfNotNull(seasonYear),
                    popularityLesser = Optional.presentIfNotNull(popularityLesser),
                    averageScoreGreater = Optional.presentIfNotNull(averageScoreGreater),
                    status = Optional.presentIfNotNull(status),
                    format = Optional.presentIfNotNull(format)
                )
            ).execute()

            // Mapeo a tu modelo de dominio
            response.data?.Page?.media?.filterNotNull()?.map { it.toDomain() } ?: emptyList()
        } catch (e: Exception) {
            emptyList() // En caso de error, devolvemos lista vacía para no romper la UI
        }
    }

    fun loadDiscoverSections() {
        viewModelScope.launch {
            loadNextPageForSection("trending", sort = listOf(MediaSort.TRENDING_DESC))
            loadNextPageForSection("action", genre = "Action", sort = listOf(MediaSort.POPULARITY_DESC))
            loadNextPageForSection("romance", genre = "Romance", sort = listOf(MediaSort.POPULARITY_DESC))
            loadNextPageForSection("adventure", genre = "Adventure", sort = listOf(MediaSort.POPULARITY_DESC))
            loadNextPageForSection("horror", genre = "Horror", sort = listOf(MediaSort.POPULARITY_DESC))
            loadNextPageForSection("thriller", genre = "Thriller", sort = listOf(MediaSort.POPULARITY_DESC))
            loadNextPageForSection("psychological", genre = "Psychological", sort = listOf(MediaSort.POPULARITY_DESC))
            loadNextPageForSection("upcoming", status = MediaStatus.NOT_YET_RELEASED, sort = listOf(MediaSort.POPULARITY_DESC))
            loadNextPageForSection("gems", popularityLesser = 50000, averageScoreGreater = 75)
            loadNextPageForSection("movies", format = MediaFormat.MOVIE, sort = listOf(MediaSort.POPULARITY_DESC))
        }
    }

    fun loadNextPageForSection(
        sectionId: String,
        genre: String? = null,
        sort: List<MediaSort>? = null,
        season: MediaSeason? = null,
        seasonYear: Int? = null,
        popularityLesser: Int? = null,
        averageScoreGreater: Int? = null,
        format: MediaFormat? = null,
        status: MediaStatus? = null
    ) {
        val currentState = _sectionsState[sectionId] ?: SectionState()
        
        if (currentState.isFetching || !currentState.hasNextPage) return

        viewModelScope.launch {
            _sectionsState[sectionId] = currentState.copy(isFetching = true)
            
            val newList = fetchDiscoverAnimes(
                genre = genre,
                sort = sort,
                season = season,
                seasonYear = seasonYear,
                popularityLesser = popularityLesser,
                averageScoreGreater = averageScoreGreater,
                format = format,
                status = status,
                page = currentState.currentPage
            )

            if (newList.isEmpty()) {
                _sectionsState[sectionId] = currentState.copy(
                    isFetching = false,
                    hasNextPage = false
                )
            } else {
                _sectionsState[sectionId] = currentState.copy(
                    list = currentState.list + newList,
                    currentPage = currentState.currentPage + 1,
                    isFetching = false
                )
                
                // Actualizamos las listas individuales para mantener compatibilidad con la UI actual si es necesario
                // Pero lo ideal es que la UI use getListForSection
                updateCompatibilityLists(sectionId, currentState.list + newList)
            }
        }
    }

    private fun updateCompatibilityLists(sectionId: String, fullList: List<AnimeManga>) {
        when (sectionId) {
            "trending" -> trendingList = fullList
            "action" -> actionList = fullList
            "romance" -> romanceList = fullList
            "adventure" -> adventureList = fullList
            "horror" -> horrorList = fullList
            "thriller" -> thrillerList = fullList
            "psychological" -> psychologicalList = fullList
            "upcoming" -> hypedUpcomingList = fullList
            "gems" -> hiddenGemsList = fullList
            "movies" -> moviesList = fullList
        }
    }

    /**
     * Busca un anime por ID en todas las listas disponibles del ViewModel.
     * Esto evita pantallas negras al navegar desde diferentes secciones.
     */
    fun findAnimeById(id: Int): AnimeManga? {
        val allLists = listOf(
            animeMangaDefaultList,
            trendingList,
            actionList,
            romanceList,
            adventureList,
            horrorList,
            hypedUpcomingList,
            hiddenGemsList,
            moviesList
        )
        return allLists.flatten().find { it.id == id }
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

            try{
                _translations[id] = "Traduciendo con IA..."
                val result = translator.translateText(anime.description.cleanHtml())

                if (result.isNotBlank()) {
                    _translations[id] = result
                    
                    // Tras traducir la sinopsis con éxito, se guarda en la db
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
                } else {
                    _translations[id] = "Error: La IA devolvió un texto vacío"
                }
            } catch (e : Exception){
                _translations[id] = "Error de conexión con la IA"
                e.printStackTrace()
            }
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
                // C. El DAO hace el upsert en la tabla intermedia (inserta si no existe, actualiza si existe)
                animeDao.upsertToLibrary(libraryEntry)
                
                // Actualizamos el estado local para que la UI se refresque inmediatamente
                currentLibraryEntry = libraryEntry

            }catch ( e : Exception){
                e.printStackTrace()
            }
        }
    }




}
