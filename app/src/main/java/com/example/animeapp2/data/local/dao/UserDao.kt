package com.example.animeapp2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.animeapp2.data.local.entities.UserEntity


@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registerUser(user: UserEntity) : Long

    @Query("SELECT * FROM users WHERE email_usuario = :email LIMIT 1")
    suspend fun getUserByEmail(email : String) : UserEntity?

    @Query("SELECT * FROM users WHERE nombre_usuario = :nombre LIMIT 1")
    suspend fun getUserByName(nombre : String) : UserEntity?

    @Query("UPDATE users SET cuenta_verificada = :status WHERE id_usuario = :id")
    suspend fun updateVerificationStatus(id: Int, status: Boolean)
}

