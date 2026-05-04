package com.example.animeapp2.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.example.animeapp2.GetAnimeMangasListByQuery
import com.example.animeapp2.GetAnimeMangasListQuery
import com.example.animeapp2.GetDiscoverAnimeMangasQuery
import com.example.animeapp2.data.local.dao.AnimeMangaDao
import com.example.animeapp2.data.local.entities.AnimeMangaEntity
import com.example.animeapp2.data.local.entities.AnimeMangaUserCrossRef
import com.example.animeapp2.data.local.entities.TranslationEntity
import com.example.animeapp2.data.mapper.toDomain
import com.example.animeapp2.data.model.AnimeManga
import com.example.animeapp2.type.MediaFormat
import com.example.animeapp2.type.MediaSeason
import com.example.animeapp2.type.MediaSort
import com.example.animeapp2.type.MediaStatus
import com.example.animeapp2.util.TranslatorManager
import com.example.animeapp2.util.cleanHtml

class AnimeRepository(
    private val apolloClient: ApolloClient,
    private val animeDao: AnimeMangaDao
) {
    private val translator = TranslatorManager()

    suspend fun getAnimeList(page: Int): List<AnimeManga> {
        val response = apolloClient.query(GetAnimeMangasListQuery(page = Optional.present(page))).execute()
        return response.data?.Page?.media?.filterNotNull()?.map { it.toDomain() } ?: emptyList()
    }

    suspend fun searchAnime(query: String, page: Int): List<AnimeManga> {
        val response = apolloClient.query(GetAnimeMangasListByQuery(
            search = Optional.present(query),
            page = Optional.present(page)
        )).execute()
        return response.data?.Page?.media?.filterNotNull()?.map { it.toDomain() } ?: emptyList()
    }

    suspend fun getDiscoverAnimes(
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
        return response.data?.Page?.media?.filterNotNull()?.map { it.toDomain() } ?: emptyList()
    }

    // Lógica de traducción
    suspend fun getTranslationFromDb(animeId: Int): String? = animeDao.getTranslation(animeId)

    suspend fun translateAndSave(anime: AnimeManga): String {
        val translatedText = translator.translateText(anime.description.cleanHtml())
        if (translatedText.isNotBlank()) {
            animeDao.insertAnimeManga(
                AnimeMangaEntity(
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
                    descripcion_traducida = translatedText
                )
            )
        }
        return translatedText
    }

    // Lógica de biblioteca
    suspend fun getLibraryEntry(userId: Int, animeId: Int) = animeDao.getUserLibraryEntry(userId, animeId)
    
    suspend fun saveToLibrary(crossRef: AnimeMangaUserCrossRef, anime: AnimeManga) {
        // Aseguramos que el anime existe en la DB local antes de crear la relación
        animeDao.insertAnimeManga(
            AnimeMangaEntity(
                id_animemanga = anime.id,
                titulo_romaji = anime.title.romaji,
                titulo_english = anime.title.english,
                titulo_native = anime.title.native,
                descripcion = anime.description,
                tipo = anime.type.name,
                portada_url = anime.coverImage.large
            )
        )
        animeDao.upsertToLibrary(crossRef)
    }

    fun getUserLibrary(userId: Int) = animeDao.getUserLibrary(userId)
    
    suspend fun removeFromLibrary(userId: Int, animeId: Int) = animeDao.removeFromLibrary(userId, animeId)
}
