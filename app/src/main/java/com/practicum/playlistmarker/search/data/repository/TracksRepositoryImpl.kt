package com.practicum.playlistmarker.search.data.repository

import com.practicum.playlistmarker.player.domain.model.Track
import com.practicum.playlistmarker.search.data.NetworkClient
import com.practicum.playlistmarker.search.data.converters.TrackFavoriteConvertorFromDatabase
import com.practicum.playlistmarker.search.data.dto.TrackDto
import com.practicum.playlistmarker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmarker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmarker.search.domain.api.TracksRepository
import com.practicum.playlistmarker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val trackFavoriteConvertorFromDatabase: TrackFavoriteConvertorFromDatabase,
) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                with(response as TracksSearchResponse) {
                    val tracksDtoList = results.map {
                        TrackDto(
                            trackName = it.trackName,
                            artistName = it.artistName,
                            trackTimeMillis = it.trackTimeMillis,
                            artworkUrl100 = it.artworkUrl100,
                            trackId = it.trackId,
                            collectionName = it.collectionName,
                            releaseDate = it.releaseDate,
                            primaryGenreName = it.primaryGenreName,
                            country = it.country,
                            previewUrl = it.previewUrl,
                        )
                    }
                    emit(
                        Resource.Success(
                            trackFavoriteConvertorFromDatabase.getFavoriteTracks(
                                tracksDtoList
                            )
                        )
                    )
                }
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}