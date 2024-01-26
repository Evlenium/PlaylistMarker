package com.practicum.playlistmarker.search.data.repository

import com.practicum.playlistmarker.search.data.NetworkClient
import com.practicum.playlistmarker.search.data.dto.TrackDto
import com.practicum.playlistmarker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmarker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmarker.search.domain.api.TracksRepository
import com.practicum.playlistmarker.util.Resource

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
) : TracksRepository {
    override fun searchTracks(expression: String): Resource<List<TrackDto>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {

                Resource.Success((response as TracksSearchResponse).results.map {
                    TrackDto(
                        it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.trackId,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl,
                    )
                })
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}