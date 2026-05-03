package com.example.animeapp2.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class UserLibraryItem(
    @Embedded val libraryEntry: AnimeMangaUserCrossRef,
    @Relation(
        parentColumn = "id_animemanga",
        entityColumn = "id_animemanga"
    )
    val anime: AnimeMangaEntity
)
