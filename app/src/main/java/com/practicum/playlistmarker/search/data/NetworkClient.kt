package com.practicum.playlistmarker.search.data

import com.practicum.playlistmarker.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}