package com.example.animeapp2.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.animeapp2.data.model.AnimeStatus

@Entity(
    tableName = "user_anime_library",
    primaryKeys = ["id_usuario", "id_animemanga"],
    foreignKeys = [
        ForeignKey(
            entity = AnimeMangaEntity::class,
            parentColumns = ["id_animemanga"],
            childColumns = ["id_animemanga"]
        ),
        ForeignKey(entity = AnimeMangaEntity::class,
            parentColumns = ["id_animemanga"],
            childColumns = ["id_animemanga"])
    ]

)

data class AnimeMangaUserCrossRef(
    val id_usuario: Int,
    val id_animemanga : Int,
    val estado : AnimeStatus = AnimeStatus.PENDIENTE, // Estado de visualización
    val episodios_vistos: Int = 0,
    val nota_personal: Int? = null,
    val es_favorito: Boolean = false,
    val fecha_agregado: Long = System.currentTimeMillis()

    )