package com.practicum.playlistmarker.search.presentation.model

sealed interface StateFavorite {
    object Favorite : StateFavorite
    object NotFavorite : StateFavorite
}