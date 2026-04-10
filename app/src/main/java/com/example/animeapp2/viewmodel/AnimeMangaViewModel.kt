package com.example.animeapp2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.Optional
import com.example.animeapp2.GetAnimeMangasListQuery
import com.example.animeapp2.data.mapper.toDomain
import com.example.animeapp2.data.model.AnimeManga
import com.example.animeapp2.data.network.apolloClient
import kotlinx.coroutines.launch

class AnimeMangaViewModel : ViewModel() {    // Esta clase hereda de ViewModel
    var animeMangaList by mutableStateOf<List<AnimeManga>>(emptyList()) // En esta lista dinamica guardamos los datos que se traen de la api
    private set
    var currentPage = 1 // Esta variable se usa para indicarle a la query que conjunto de AnimeMangas traer
    private set

    var isFetching by mutableStateOf(false) // Esta variable indica si estamos cargando mas datos
        private set

    fun fetchAnimes() {
        viewModelScope.launch {
            if (isFetching) return@launch // Si ya estamos cargando, no hacemos nada

            try {
                isFetching = true // Indicamos que ahora si estamos cargando
                // Paso 1: Traer los datos de la API

                val response = apolloClient.query(GetAnimeMangasListQuery(page = Optional.present(currentPage))).execute()
                val remoteAnimeMangas = response.data?.Page?.media?.filterNotNull() ?: emptyList()
                // Paso 2: Iterar sobre los datos traidos, mapearlos a objetos AnimeManga y agregarlos a la lista para mostrarlos en la UI
                animeMangaList += remoteAnimeMangas.map { it.toDomain() }
                currentPage++
            } catch (e: Exception) {
                e.printStackTrace()
            }
            finally {
                isFetching = false // Independientemente de si hubo error o no, indicamos que ya no estamos cargando mas datos
            }
        }
    }
}