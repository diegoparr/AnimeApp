package com.example.animeapp2.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.animeapp2.data.local.dao.AnimeMangaDao
import com.example.animeapp2.data.local.dao.UserDao
import com.example.animeapp2.data.local.entities.*

@Database(
    entities = [
        UserEntity::class,
        AnimeMangaEntity::class,
        AnimeGenreCrossRef::class,
        TranslationEntity::class,
        AnimeMangaUserCrossRef::class,
        GenreEntity::class
    ],
    version = 2



)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun userDao() : UserDao
    abstract fun animeMangaDao() : AnimeMangaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "crimson_list_db"
                )
                .fallbackToDestructiveMigration(true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

