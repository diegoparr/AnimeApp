package com.example.animeapp2.util
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
    class TranslatorManager {
        val model = Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel("gemini-2.5-flash")

        suspend fun translateText(text: String): String {
            return try {
                // Le decimos a Gemini exactamente qué queremos.
                val prompt = """
                Traduce el siguiente texto de sinopsis de anime del inglés al español.
                Asegúrate de que la traducción sea fluida, profesional y respete 
                los términos técnicos del mundo del anime.
                No añadas comentarios tuyos, solo devuelve el texto traducido.
                
                Texto: $text
            """.trimIndent()

                val response = model.generateContent(prompt)
                response.text ?: text // Si la IA no responde, devolvemos la sinopsis original
            } catch (e: Exception) {
                e.printStackTrace()
                text
            }
        }
    }






