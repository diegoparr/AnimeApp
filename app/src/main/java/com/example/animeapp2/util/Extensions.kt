package com.example.animeapp2.util

fun String.cleanHtml(): String {
    return android.text.Html.fromHtml(this, android.text.Html.FROM_HTML_MODE_LEGACY).toString()
}

