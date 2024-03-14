package com.practicum.playlistmarker.search.data.repository

import com.practicum.playlistmarker.search.data.NetworkClient
import com.practicum.playlistmarker.search.data.dto.TrackDto
import com.practicum.playlistmarker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmarker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmarker.search.domain.api.TracksRepository
import com.practicum.playlistmarker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<TrackDto>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                with(response as TracksSearchResponse) {
                    val data = results.map {
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
                    }
                    emit(Resource.Success(data))
                }
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}