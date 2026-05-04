package com.example.animeapp2.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.animeapp2.data.local.AppDatabase
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import com.example.animeapp2.data.local.entities.UserEntity
import com.example.animeapp2.util.SecurityHelper
import com.example.animeapp2.util.isValidEmail
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
class AuthUsersViewModel (application : Application) : AndroidViewModel(application){

    // Inicializar DAO
    private val userDao = AppDatabase.getDatabase(application).userDao()

    // Inicializar autenticacion por email con Firebase
    val auth = FirebaseAuth.getInstance()
    // 1. Control de carga
    var isLoading by mutableStateOf(false)
        private set

    // 2. Control de errores (se limpia al intentar de nuevo)
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

    /**
     * REGISTRO CON HASH DE CONTRASEÑA
     */
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

                if (userDao.getUserByName(nombre) != null) {
                    errorMessage = "El nombre de usuario ya está en uso"
                    return@launch
                }

                if (userDao.getUserByEmail(email) != null) {
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
                auth.createUserWithEmailAndPassword(email, password).await()

                // 2. Email Verification
                auth.currentUser?.sendEmailVerification()?.await()
                isVerificationEmailSent = true

                // 3. Local database registration
                val newUser = UserEntity(
                    nombre_usuario = nombre,
                    email_usuario = email,
                    contraseña_hash = SecurityHelper.hashPassword(password),
                    cuenta_verificada = false // Por defecto es false
                )

                userDao.registerUser(newUser)
                currentUser = newUser
                isAuthSuccess = true

            } catch (e: Exception) {
                errorMessage = "Error de registro: ${e.localizedMessage ?: e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * LOGIN CON COMPARACION DE HASH
     */
    fun login(email : String, password : String) {
        viewModelScope.launch {
            isLoading = true
            resetStatus()

            try {
                // 1. Intentar ingresar a Firebase (AHORA ESPERANDO RESPUESTA)
                auth.signInWithEmailAndPassword(email, password).await()

                // 2. Forzar actualización para ver si ya se verificó el email
                auth.currentUser?.reload()?.await()

                // 3. Verificar si el email está verificado
                if (auth.currentUser?.isEmailVerified == true) {
                    val user = userDao.getUserByEmail(email)
                    if (user != null) {
                        // Sincronizar estado de verificación con DB local si es necesario
                        if (!user.cuenta_verificada) {
                            userDao.updateVerificationStatus(user.id_usuario, true)
                        }

                        // Comprobacion de contraseña ingresada con contraseña en db hasheada
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
                    auth.signOut() // Importante cerrar sesión si no está verificado
                }
            } catch (e : Exception) {
                // Este catch capturará errores de Firebase (como "Password incorrecto")
                errorMessage = "Error de autenticación: ${e.localizedMessage ?: e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun loadCurrentUserId(){
        viewModelScope.launch {
            val email = auth.currentUser?.email
            if (email != null) {
                currentUserId = userDao.getUserIdByEmail(email)
            }
        }
    }

    fun logout() {
        currentUser = null    // Ya no hay usuario
        errorMessage = null   // Limpiamos errores
        isAuthSuccess = false // Apagamos el interruptor de éxito
    }

    fun updateUsername(nuevoNombre: String) {
        val user = currentUser ?: return
        if (nuevoNombre.isBlank()) {
            errorMessage = "El nombre no puede estar vacío"
            return
        }

        viewModelScope.launch {
            try {
                // Verificar si el nombre ya existe
                val existingUser = userDao.getUserByName(nuevoNombre)
                if (existingUser != null && existingUser.id_usuario != user.id_usuario) {
                    errorMessage = "Este nombre de usuario ya está en uso"
                    return@launch
                }

                userDao.updateUsername(user.id_usuario, nuevoNombre)
                // Actualizar el estado local
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