package com.practicum.playlistmarker.sharing.domain.api

interface SharingInteractor {
    fun shareApp()
    fun openTerms()
    fun openSupport()

    fun getMessageAddedToPlaylist(): String

    fun getMessageAddedToPlaylistYet(): String
    fun sharePlaylist(message: String)

    fun getPluralsTrack(counterTracks: Int): String
}