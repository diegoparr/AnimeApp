package com.example.animeapp2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.animeapp2.data.local.entities.AnimeMangaEntity
import com.example.animeapp2.data.local.entities.AnimeMangaUserCrossRef
import com.example.animeapp2.data.local.entities.TranslationEntity

@Dao
interface AnimeMangaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeManga(animeManga : AnimeMangaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation : TranslationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToLibrary(animeManga : AnimeMangaUserCrossRef)


    @Query("SELECT descripcion_traducida FROM anime_translations WHERE id_animemanga = :animeId AND lenguaje = :lang")
    suspend fun getTranslation(animeId : Int, lang: String = "es") : String?

    @Upsert
    suspend fun upsertToLibrary(crossRef: AnimeMangaUserCrossRef)

    @Query("SELECT * FROM user_anime_library WHERE id_usuario = :userId AND id_animemanga = :animeId")
    suspend fun getUserLibraryEntry(userId: Int, animeId: Int): AnimeMangaUserCrossRef?
}
