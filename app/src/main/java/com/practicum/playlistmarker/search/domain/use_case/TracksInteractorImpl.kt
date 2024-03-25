package com.practicum.playlistmarker.search.domain.use_case

import com.practicum.playlistmarker.player.domain.model.Track
import com.practicum.playlistmarker.search.domain.MapperDto
import com.practicum.playlistmarker.search.domain.api.TracksInteractor
import com.practicum.playlistmarker.search.domain.api.TracksRepository
import com.practicum.playlistmarker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(
                        result.data,
                        null
                    )
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}
