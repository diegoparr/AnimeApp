package com.example.animeapp2.ui.navigation

sealed class Screen(val route : String){
    
    // Pantalla Principal (Lista de Animes)
    object Home : Screen("AnimeMangaListHomeScreen")
    
    // Pantalla de Detalle (acepta un argumento: animeId)
    object Detail : Screen("AnimeMangaDetailScreen/{animeId}"){
        fun createRoute(animeId : Int) = "AnimeMangaDetailScreen/$animeId"
    }
    
    // Tu biblioteca personal (Favoritos)
    object MyList : Screen("MyListScreen")
    
    // La pantalla de Descubrir / Búsqueda
    object Search : Screen("DiscoverScreen")
}
