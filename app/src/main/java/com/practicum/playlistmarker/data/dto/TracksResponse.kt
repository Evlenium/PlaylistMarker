package com.practicum.playlistmarker.data.dto

import com.practicum.playlistmarker.domain.model.TrackSearchItem

class TracksResponse (val resultCount: Int,
                      val results: List<TrackSearchItem.Track>)