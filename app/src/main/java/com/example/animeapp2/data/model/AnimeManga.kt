package com.example.animeapp2.data.model

import com.example.animeapp2.data.model.CoverImage
import com.example.animeapp2.data.model.Title

data class AnimeManga(val id: Int,
                      val title: Title,
                      val type: MediaType,
                      val genres: List<String>,
                      val description: String,
                      val coverImage: CoverImage,
                      val averageScore: Int?
)