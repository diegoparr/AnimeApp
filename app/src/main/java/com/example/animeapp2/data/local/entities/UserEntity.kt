package com.example.animeapp2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id_usuario: Int = 0,
    val email_usuario : String,
    val nombre_usuario: String,
    val contraseña_hash : String,
    val cuenta_verificada: Boolean = false
)