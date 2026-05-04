package com.example.animeapp2.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.animeapp2.data.local.AppDatabase
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import com.example.animeapp2.data.local.entities.UserEntity
import com.example.animeapp2.data.repository.AuthRepository
import com.example.animeapp2.util.SecurityHelper
import com.example.animeapp2.util.isValidEmail
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthUsersViewModel (application : Application) : AndroidViewModel(application){

    private val authRepository = AuthRepository(
        FirebaseAuth.getInstance(),
        AppDatabase.getDatabase(application).userDao()
    )

    // Para compatibilidad con la UI
    val auth get() = FirebaseAuth.getInstance()

    // 1. Control de carga
    var isLoading by mutableStateOf(false)
        private set

    // 2. Control de errores
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // 3. Usuario en sesion
    var currentUser by mutableStateOf<UserEntity?>(null)
        private set

    var currentUserId by mutableStateOf<Int?>(null)
        private set

    // 4. Check de navegacion
    var isAuthSuccess by mutableStateOf(false)
        private set

    var isVerificationEmailSent by mutableStateOf(false)
        private set

    fun register(nombre: String, email: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            resetStatus()
            try {
                if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    errorMessage = "Todos los campos son obligatorios"
                    return@launch
                }

                if (!email.isValidEmail()) {
                    errorMessage = "El email no es válido"
                    return@launch
                }

                if (authRepository.getUserByName(nombre) != null) {
                    errorMessage = "El nombre de usuario ya está en uso"
                    return@launch
                }

                if (authRepository.getUserByEmail(email) != null) {
                    errorMessage = "El correo ya está en uso"
                    return@launch
                }

                if (password.length < 6) {
                    errorMessage = "La contraseña debe tener al menos 6 caracteres"
                    return@launch
                }

                if (password.contains(" ")) {
                    errorMessage = "La contraseña no puede contener espacios"
                    return@launch
                }

                // 1. Firebase Authentication
                authRepository.register(email, password)

                // 2. Email Verification
                authRepository.getCurrentFirebaseUser()?.sendEmailVerification()?.await()
                isVerificationEmailSent = true

                // 3. Local database registration
                val newUser = UserEntity(
                    nombre_usuario = nombre,
                    email_usuario = email,
                    contraseña_hash = SecurityHelper.hashPassword(password),
                    cuenta_verificada = false
                )

                authRepository.registerUser(newUser)
                currentUser = newUser
                isAuthSuccess = true

            } catch (e: Exception) {
                errorMessage = "Error de registro: ${e.localizedMessage ?: e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun login(email : String, password : String) {
        viewModelScope.launch {
            isLoading = true
            resetStatus()

            try {
                authRepository.login(email, password)
                val firebaseUser = authRepository.getCurrentFirebaseUser()
                firebaseUser?.reload()?.await()

                if (firebaseUser?.isEmailVerified == true) {
                    val user = authRepository.getUserByEmail(email)
                    if (user != null) {
                        if (!user.cuenta_verificada) {
                            authRepository.updateVerificationStatus(user.id_usuario, true)
                        }

                        if (SecurityHelper.checkPassword(password, user.contraseña_hash)) {
                            currentUser = user.copy(cuenta_verificada = true)
                            isAuthSuccess = true
                        } else {
                            errorMessage = "Contraseña incorrecta"
                        }
                    } else {
                        errorMessage = "Usuario no encontrado"
                    }
                } else {
                    errorMessage = "Tu cuenta aún no ha sido verificada. Revisa tu correo."
                    authRepository.signOut()
                }
            } catch (e : Exception) {
                errorMessage = "Error de autenticación: ${e.localizedMessage ?: e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun loadCurrentUserId(){
        viewModelScope.launch {
            val email = authRepository.getCurrentFirebaseUser()?.email
            if (email != null) {
                currentUserId = authRepository.getUserIdByEmail(email)
            }
        }
    }

    fun logout() {
        authRepository.signOut()
        currentUser = null
        errorMessage = null
        isAuthSuccess = false
    }

    fun updateUsername(nuevoNombre: String) {
        val user = currentUser ?: return
        if (nuevoNombre.isBlank()) {
            errorMessage = "El nombre no puede estar vacío"
            return
        }

        viewModelScope.launch {
            try {
                val existingUser = authRepository.getUserByName(nuevoNombre)
                if (existingUser != null && existingUser.id_usuario != user.id_usuario) {
                    errorMessage = "Este nombre de usuario ya está en uso"
                    return@launch
                }

                authRepository.updateUsername(user.id_usuario, nuevoNombre)
                currentUser = user.copy(nombre_usuario = nuevoNombre)
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = "Error al actualizar el nombre: ${e.message}"
            }
        }
    }

    fun resetStatus() {
        errorMessage = null
        isAuthSuccess = false
    }
}
