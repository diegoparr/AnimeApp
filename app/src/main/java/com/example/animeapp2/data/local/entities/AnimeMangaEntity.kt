package com.example.animeapp2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime_manga")
data class AnimeMangaEntity(
    @PrimaryKey val id_animemanga : Int,
    val titulo_romaji: String,
    val titulo_english: String?,
    val titulo_native: String?,
    val descripcion: String,
    val tipo: String,
    val portada_url: String
)