package com.practicum.playlistmarker.search.domain.use_case

import com.practicum.playlistmarker.search.domain.MapperDto
import com.practicum.playlistmarker.search.domain.api.TracksInteractor
import com.practicum.playlistmarker.search.domain.api.TracksRepository
import com.practicum.playlistmarker.util.Resource
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {

            when (val resource = repository.searchTracks(expression)) {
                is Resource.Success -> {
                    consumer.consume((resource.data)?.map { trackDto ->
                        MapperDto.mapFromTrackToTrackDto(
                            trackDto
                        )
                    }, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }
            }
        }
    }
}