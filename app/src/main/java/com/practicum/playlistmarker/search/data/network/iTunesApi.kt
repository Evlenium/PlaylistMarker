package com.practicum.playlistmarker.search.data.network

import com.practicum.playlistmarker.search.data.dto.TracksSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesApi {
    @GET("/search?entity=song")
    suspend fun searchTracks(@Query("term") text: String): Response<TracksSearchResponse>
}