package com.example.animeapp2.data.local

import androidx.room.TypeConverter
import com.example.animeapp2.data.model.AnimeStatus
import com.example.animeapp2.data.model.MediaType

class Converters{

    @TypeConverter
    fun fromStatus(status : AnimeStatus): String = status.name

    @TypeConverter
    fun toStatus(value: String): AnimeStatus = try{
        AnimeStatus.valueOf(value)
    } catch (e: Exception){
        AnimeStatus.PENDIENTE // Valor por defecto si algo falla
    }

    @TypeConverter
    fun fromMediaType(type: MediaType): String = type.name

    @TypeConverter
    fun toMediaType(value: String): MediaType = try {
        MediaType.valueOf(value)
    } catch (e: Exception) {
        MediaType.DESCONOCIDO
    }

    @TypeConverter
    fun fromList(list: List<String>): String = list.joinToString(",")

    @TypeConverter
    fun toList(data: String): List<String> = data.split(",").filter { it.isNotEmpty() }

}
