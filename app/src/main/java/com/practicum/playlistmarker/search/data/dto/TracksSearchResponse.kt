package com.practicum.playlistmarker.search.data.dto

class TracksSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>,
) : Response()