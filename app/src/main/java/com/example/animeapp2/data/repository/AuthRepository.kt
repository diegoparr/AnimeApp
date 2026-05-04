package com.example.animeapp2.data.repository

import com.example.animeapp2.data.local.dao.UserDao
import com.example.animeapp2.data.local.entities.UserEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth,
    private val userDao: UserDao
) {
    fun getCurrentFirebaseUser() = auth.currentUser

    suspend fun login(email: String, password: String) = auth.signInWithEmailAndPassword(email, password).await()

    suspend fun register(email: String, password: String) = auth.createUserWithEmailAndPassword(email, password).await()

    fun signOut() = auth.signOut()

    suspend fun getUserByEmail(email: String) = userDao.getUserByEmail(email)
    
    suspend fun getUserIdByEmail(email: String) = userDao.getUserIdByEmail(email)
    
    suspend fun getUserByName(nombre: String) = userDao.getUserByName(nombre)
    
    suspend fun registerUser(user: UserEntity) = userDao.registerUser(user)
    
    suspend fun updateVerificationStatus(id: Int, status: Boolean) = userDao.updateVerificationStatus(id, status)
    
    suspend fun updateUsername(id: Int, nuevoNombre: String) = userDao.updateUsername(id, nuevoNombre)
}
