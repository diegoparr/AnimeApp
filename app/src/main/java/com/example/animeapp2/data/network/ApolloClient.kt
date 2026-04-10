package com.example.animeapp2.data.network

import com.apollographql.apollo.ApolloClient

// Este archivo incializa el cliente Apollo para consumir la API de AniList.
val apolloClient = ApolloClient.Builder()
    .serverUrl("https://graphql.anilist.co")
    .build()