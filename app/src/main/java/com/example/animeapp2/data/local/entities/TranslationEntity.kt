package com.example.animeapp2.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "anime_translations",
    primaryKeys = ["id_animemanga","lenguaje"],
    foreignKeys = [
        ForeignKey(
            entity = AnimeMangaEntity::class,
            parentColumns = ["id_animemanga"],
            childColumns = ["id_animemanga"],
            onDelete = ForeignKey.CASCADE // Si se borra el anime, se borran sus traducciones
        )
    ]
)
data class TranslationEntity(
    val id_animemanga: Int,
    val lenguaje: String, // "es", "en", etc.
    val descripcion_traducida: String,
    val last_updated: Long = System.currentTimeMillis()
)
