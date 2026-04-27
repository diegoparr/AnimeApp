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
                val existingUser = userDao.getUserByEmail(email)
                if(existingUser != null){
                    errorMessage = "El correo ya está en uso"
                }
                else if (userDao.getUserByName(nombre) != null){
                    errorMessage = "El nombre de usuario ya está en uso"
                }
                else if (password.length < 6 ){
                    errorMessage = "La contraseña debe tener al menos 6 caracteres"
                }
                else {
                    val newUser = UserEntity(nombre_usuario = nombre, email_usuario = email, contraseña_hash = SecurityHelper.hashPassword(password))
                    userDao.registerUser(newUser)
                    currentUser = newUser
                    isAuthSuccess = true
                }
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

    fun resetStatus() {
        errorMessage = null
        isAuthSuccess = false
    }

}