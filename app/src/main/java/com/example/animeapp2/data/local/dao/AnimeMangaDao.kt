package com.example.animeapp2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.animeapp2.data.local.entities.AnimeMangaEntity
import com.example.animeapp2.data.local.entities.AnimeMangaUserCrossRef
import com.example.animeapp2.data.local.entities.TranslationEntity
import com.example.animeapp2.data.local.entities.UserLibraryItem
import kotlinx.coroutines.flow.Flow

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

    @Transaction
    @Query("SELECT * FROM user_anime_library WHERE id_usuario = :userId ORDER BY fecha_agregado DESC")
    fun getUserLibrary(userId: Int): Flow<List<UserLibraryItem>>

    @Query("DELETE FROM user_anime_library WHERE id_usuario = :userId AND id_animemanga = :animeId")
    suspend fun removeFromLibrary(userId: Int, animeId: Int)
}



