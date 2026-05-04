package com.example.animeapp2.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp2.data.local.AppDatabase
import com.example.animeapp2.data.local.entities.AnimeMangaUserCrossRef
import com.example.animeapp2.data.model.AnimeManga
import com.example.animeapp2.data.model.AnimeStatus
import com.example.animeapp2.data.network.apolloClient
import com.example.animeapp2.data.repository.AnimeRepository
import com.example.animeapp2.type.MediaFormat
import com.example.animeapp2.type.MediaSeason
import com.example.animeapp2.type.MediaSort
import com.example.animeapp2.type.MediaStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AnimeMangaViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = AnimeRepository(
        apolloClient,
        AppDatabase.getDatabase(application).animeMangaDao()
    )

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

    var searchText by mutableStateOf("")
    var isSearchActive by mutableStateOf(false)

    var hasNextPage by mutableStateOf(true)
        private set

    // Estado para la entrada actual en la biblioteca del usuario
    var currentLibraryEntry by mutableStateOf<AnimeMangaUserCrossRef?>(null)
        private set

    private val _sectionsState = mutableStateMapOf<String, SectionState>()

    fun getListForSection(section: String) = _sectionsState[section]?.list ?: emptyList()
    fun isSectionFetching(section: String) = _sectionsState[section]?.isFetching ?: false

    // Estado sincronizado con la DB para identificar id de usuario
    private val _currentUserId = MutableStateFlow<Int?>(null)

    fun setUserId(userId: Int?) {
        _currentUserId.value = userId
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _dbLibraryFlow = _currentUserId.flatMapLatest { userId ->
        if (userId == null) flowOf(emptyList())
        else repository.getUserLibrary(userId)
    }

    var selectedLibraryFilter by mutableStateOf<AnimeStatus?>(null)

    val filteredLibrary = combine(
        _dbLibraryFlow,
        snapshotFlow { selectedLibraryFilter }
    ) { list, filter ->
        if (filter == null) list
        else list.filter { it.libraryEntry.estado == filter }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    data class SectionState(
        val list: List<AnimeManga> = emptyList(),
        val currentPage: Int = 1,
        val hasNextPage: Boolean = true,
        val isFetching: Boolean = false
    )

    private val _translations = mutableStateMapOf<Int, String>()

    fun getTranslationFor(id: Int): String = _translations[id] ?: ""

    fun loadLibraryEntry(userId: Int, animeId: Int) {
        viewModelScope.launch {
            currentLibraryEntry = repository.getLibraryEntry(userId, animeId)
        }
    }

    fun loadTranslationFromDb(animeId: Int) {
        viewModelScope.launch {
            val cached = repository.getTranslationFromDb(animeId)
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
                    searchText = ""
                    isSearchActive = false
                    hasNextPage = true
                }
                
                val newList = repository.getAnimeList(currentPage)
                
                if (newList.isEmpty()) {
                    hasNextPage = false
                } else {
                    animeMangaDefaultList += newList
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

        if (isNewSearch && animeName == lastSearchQuery && animeMangaDefaultList.isNotEmpty()) return

        viewModelScope.launch {
            if (isFetching) return@launch
            try {
                isFetching = true

                if(isNewSearch) {
                    currentPage = 1
                    animeMangaDefaultList = emptyList()
                    lastSearchQuery = animeName
                    isSearchActive = true
                    hasNextPage = true
                }
                
                val newList = repository.searchAnime(animeName, currentPage)

                if (newList.isEmpty()) {
                    hasNextPage = false
                } else {
                    animeMangaDefaultList += newList
                    currentPage++
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isFetching = false
            }
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
            
            val newList = repository.getDiscoverAnimes(
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
                val fullList = currentState.list + newList
                _sectionsState[sectionId] = currentState.copy(
                    list = fullList,
                    currentPage = currentState.currentPage + 1,
                    isFetching = false
                )
                updateCompatibilityLists(sectionId, fullList)
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

    fun findAnimeById(id: Int): AnimeManga? {
        val allLists = listOf(
            animeMangaDefaultList,
            trendingList,
            actionList,
            romanceList,
            adventureList,
            horrorList,
            thrillerList,
            psychologicalList,
            hypedUpcomingList,
            hiddenGemsList,
            moviesList
        )
        return allLists.flatten().find { it.id == id }
    }

    fun loadMore() {
        if (isFetching || !hasNextPage) return
        val query = lastSearchQuery
        if (query == null) fetchAnimes()
        else fetchAnimesByUserQuery(query, isNewSearch = false)
    }

    fun translateDescription(anime : AnimeManga) {
        val id = anime.id
        if(_translations.containsKey(id)){
            _translations.remove(id)
            return
        }

        viewModelScope.launch{
            val existingTranslation = repository.getTranslationFromDb(id)
            if(existingTranslation != null){
                _translations[id] = existingTranslation
                return@launch
            }

            try{
                _translations[id] = "Traduciendo con IA..."
                val result = repository.translateAndSave(anime)

                if (result.isNotBlank()) {
                    _translations[id] = result
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
                val libraryEntry = AnimeMangaUserCrossRef(
                    id_usuario = userId,
                    id_animemanga = animeManga.id,
                    estado = status,
                    episodios_vistos = episodesWatched,
                    nota_personal = rating,
                    es_favorito = isFavorite
                )
                
                repository.saveToLibrary(libraryEntry, animeManga)
                currentLibraryEntry = libraryEntry

            }catch ( e : Exception){
                e.printStackTrace()
            }
        }
    }
}
