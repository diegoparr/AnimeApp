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
import kotlinx.coroutines.launch
class AuthUsersViewModel (application : Application) : AndroidViewModel(application){

    private val userDao = AppDatabase.getDatabase(application).userDao()

    // 1. Control de carga
    var isLoading by mutableStateOf(false)
        private set

    // 2. Control de errores (se limpia al intentar de nuevo)
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // 3. Usuario en sesion
    var currentUser by mutableStateOf<UserEntity?>(null)
        private set

    // 4. Check de navegacion
    var isAuthSuccess by mutableStateOf(false)
        private set


    /**
     * REGISTRO CON HASH DE CONTRASEÑA
     */
    fun register(nombre : String , email : String , password : String) {
        viewModelScope.launch {
            isLoading = true
            resetStatus()
            try {
                if(nombre.isEmpty() || email.isEmpty() || password.isEmpty()){
                    errorMessage = "Todos los campos son obligatorios"
                    return@launch
                }

                if(!email.isValidEmail()){
                    errorMessage = "El email no es válido"
                    return@launch
                }

                if (userDao.getUserByName(nombre) != null){
                    errorMessage = "El nombre de usuario ya está en uso"
                    return@launch
                }

                if (userDao.getUserByEmail(email) != null) {
                    errorMessage = "El correo ya está en uso"
                    return@launch
                }

                if (password.length < 6){
                    errorMessage = "La contraseña debe tener al menos 6 caracteres"
                    return@launch
                }

                if (password.contains(" ")){
                    errorMessage = "La contraseña no puede contener espacios"
                    return@launch
                }

                val newUser = UserEntity(nombre_usuario = nombre,
                    email_usuario = email,
                    contraseña_hash = SecurityHelper.hashPassword(password))

                userDao.registerUser(newUser)
                currentUser = newUser
                isAuthSuccess = true

            }
            catch (e : Exception){
                errorMessage = "Error de registro : ${e.message} "
            }finally {
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

            try{
                val user = userDao.getUserByEmail(email)
                if(user != null){
                    if(SecurityHelper.checkPassword(password, user.contraseña_hash)){
                        currentUser = user
                        isAuthSuccess = true
                    }
                    else{
                        errorMessage = "Contraseña incorrecta"
                    }
                } else{
                    errorMessage = "Usuario no encontrado"
                }
            } catch (e : Exception){
                errorMessage = "Ocurrio un error inesperado : ${e.message}"
            }finally{
                isLoading = false
            }
        }
    }

    fun logout() {
        currentUser = null    // Ya no hay usuario
        errorMessage = null   // Limpiamos errores
        isAuthSuccess = false // Apagamos el interruptor de éxito
    }
    fun resetStatus() {
        errorMessage = null
        isAuthSuccess = false
    }

}