package com.example.animeapp2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "genero_animemanga",
)

data class GenreEntity(
    @PrimaryKey val nombre_genero : String
)