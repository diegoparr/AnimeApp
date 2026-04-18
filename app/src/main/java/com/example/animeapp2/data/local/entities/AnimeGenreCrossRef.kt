package com.example.animeapp2.data.local.entities

import androidx.room.Entity

@Entity(
    tableName = "anime_genero_cross_ref",
    primaryKeys = ["id_animemanga", "nombre_genero"],
)


data class AnimeGenreCrossRef(
    val id_animemanga : Int,
    val nombre_genero : String
)