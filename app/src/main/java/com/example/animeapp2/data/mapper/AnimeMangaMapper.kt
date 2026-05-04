package com.example.animeapp2.data.mapper

import com.example.animeapp2.GetAnimeMangasListByQuery
import com.example.animeapp2.GetAnimeMangasListQuery
import com.example.animeapp2.GetDiscoverAnimeMangasQuery
import com.example.animeapp2.data.model.AnimeManga
import com.example.animeapp2.data.model.Title
import com.example.animeapp2.data.model.CoverImage
import com.example.animeapp2.data.model.MediaType
import com.example.animeapp2.util.cleanHtml

// Este mapper transforma objetos que vengan de la API en la seccion Medium (media) a objetos Kotlin de la
// clase AnimeManga.
fun GetAnimeMangasListQuery.Medium.toDomain(): AnimeManga {
    return AnimeManga(
        id = this.id,
        // 2. Mapeamos el titulo que trae la api a un objeto Title
        title = Title(
            romaji = this.title?.romaji ?: "",
            english = this.title?.english ?: this.title?.romaji ?: "",
            native = this.title?.native ?: ""
        ),
        type = try{
            MediaType.valueOf(this.type?.toString() ?: "DESCONOCIDO")
        } catch(e: Exception){
            MediaType.DESCONOCIDO
        },
        genres = this.genres?.filterNotNull() ?: emptyList(),
        description = this.description?.cleanHtml() ?: "No description",
        // 3. Mapeamos la imagen de portada que trae la api a un objeto CoverImage
        coverImage = CoverImage(
            large = this.coverImage?.large ?: "",
            color = this.coverImage?.color ?: "#000000"
        ),
        averageScore = this.averageScore
    )
}


fun GetAnimeMangasListByQuery.Medium.toDomain() : AnimeManga{
    return AnimeManga(
        id = this.id,
        // 2. Mapeamos el titulo que trae la api a un objeto Title
        title = Title(
            romaji = this.title?.romaji ?: "",
            english = this.title?.english ?: this.title?.romaji ?: "",
            native = this.title?.native ?: ""
        ),
        type = try{
            MediaType.valueOf(this.type?.toString() ?: "DESCONOCIDO")
        } catch(e: Exception){
            MediaType.DESCONOCIDO
        },
        genres = this.genres?.filterNotNull() ?: emptyList(),
        description = this.description?.cleanHtml() ?: "No description",
        // 3. Mapeamos la imagen de portada que trae la api a un objeto CoverImage
        coverImage = CoverImage(
            large = this.coverImage?.large ?: "",
            color = this.coverImage?.color ?: "#000000"
        ),
        averageScore = this.averageScore
    )
}

fun GetDiscoverAnimeMangasQuery.Medium.toDomain(): AnimeManga {
    return AnimeManga(
        id = this.id,
        title = Title(
            romaji = this.title?.romaji ?: "",
            english = this.title?.english ?: this.title?.romaji ?: "",
            native = this.title?.native ?: ""
        ),
        type = try {
            MediaType.valueOf(this.type?.name ?: "DESCONOCIDO")
        } catch(e: Exception) {
            MediaType.DESCONOCIDO
        },
        genres = this.genres?.filterNotNull() ?: emptyList(),
        description = this.description?.cleanHtml() ?: "No description",
        coverImage = CoverImage(
            large = this.coverImage?.large ?: "",
            color = this.coverImage?.color ?: "#000000"
        ),
        averageScore = this.averageScore
    )
}