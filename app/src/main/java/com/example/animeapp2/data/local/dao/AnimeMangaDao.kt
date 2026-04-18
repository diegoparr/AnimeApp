package com.example.animeapp2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.animeapp2.data.local.entities.AnimeMangaEntity
import com.example.animeapp2.data.local.entities.TranslationEntity


@Dao
interface AnimeMangaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeManga(animeManga : AnimeMangaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation : TranslationEntity)

    @Query("SELECT descripcion_traducida FROM anime_traducciones WHERE id_animemanga = :animeId AND lenguaje = :lang ")
    suspend fun getTranslation(animeId : Int, lang: String = "es") : String?


}