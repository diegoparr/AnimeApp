package com.example.animeapp2.util

import org.mindrot.jbcrypt.BCrypt

object SecurityHelper {

    // Generar un hash a partir de la contraseña en texto plano
    fun hashPassword(password : String) : String {
        return BCrypt.hashpw(password, BCrypt.gensalt(12)
    )
    }

    fun checkPassword(password: String, hashedPassword: String) : Boolean {
        return BCrypt.checkpw(password, hashedPassword)
    }

}