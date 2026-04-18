package com.example.animeapp2.data.local

import androidx.room.TypeConverter
import com.example.animeapp2.data.model.AnimeStatus

class Converters{

    @TypeConverter
    fun fromStatus(status : AnimeStatus): String = status.name

    @TypeConverter
    fun toStatus(value: String): AnimeStatus = AnimeStatus.valueOf(value)

    @TypeConverter
    fun fromList(list: List<String>): String = list.joinToString(",")

    @TypeConverter
    fun toList(data: String): List<String> = data.split(",").filter { it.isNotEmpty() }

}