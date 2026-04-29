package com.example.animeapp2.util
import androidx.core.text.HtmlCompat


// Funcion para limpiar html que viene dentro del campo description de la API
fun String.cleanHtml(): String {
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
}

fun String.isValidEmail(): Boolean{
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}