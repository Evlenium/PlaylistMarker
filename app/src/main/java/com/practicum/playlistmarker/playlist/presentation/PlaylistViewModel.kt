package com.practicum.playlistmarker.playlist.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmarker.media_library.domain.db.api.playlist.PlaylistInteractor
import com.practicum.playlistmarker.media_library.domain.model.playlist.Playlist
import com.practicum.playlistmarker.player.domain.model.Track
import com.practicum.playlistmarker.sharing.domain.api.SharingInteractor
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {
    private val playlistTracksLiveData = MutableLiveData<List<Track>>()
    private val playlistLiveData = MutableLiveData<Playlist>()
    private val playlistLengthLiveData = MutableLiveData<Long>()
    fun observeDataInPlaylist(): LiveData<List<Track>> = playlistTracksLiveData

    fun observePlaylist(): MutableLiveData<Playlist> = playlistLiveData
    fun observeLengthPlaylist(): LiveData<Long> = playlistLengthLiveData

    fun fillDataInPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.getTracksInPlaylist(playlist.trackIdList).collect { tracks ->
                processResult(tracks)
            }
            playlistLiveData.postValue(playlistInteractor.getPlaylistById(playlistId = playlist.playlistId))
        }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isNotEmpty()) {
            var tracksTimeMills: Long = 0
            playlistTracksLiveData.postValue(tracks)
            tracks.map { track ->
                tracksTimeMills += track.trackTimeMillis.toLong()
            }
            playlistLengthLiveData.postValue(tracksTimeMills)
        } else {
            playlistTracksLiveData.postValue(emptyList())
            playlistLengthLiveData.postValue(0)
        }
    }

    fun deleteTrackFromPlaylist(trackId: String, playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(trackId, playlist)
            playlistLiveData.postValue(playlistInteractor.getPlaylistById(playlistId = playlist.playlistId))
            playlistInteractor.getTracksInPlaylist(playlist.trackIdList).collect { tracks ->
                processResult(tracks)
            }
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch { playlistInteractor.deletePlaylist(playlist) }
    }


    fun sharePlaylist() {
        sharingInteractor.sharePlaylist(messageConstructor())
    }

    private fun messageConstructor(): String {
        val messageResult = buildString {
            append("${playlistLiveData.value?.playlistName}\n")
            append("${playlistLiveData.value?.playlistDescription}\n")
            append("${sharingInteractor.getPluralsTrack(playlistLiveData.value?.counterTracks!!)}\n")
            playlistTracksLiveData.value!!.asReversed().forEachIndexed { index, track ->
                append("${(index + 1)}. ${track.artistName} - ${track.trackName} ")
                append(
                    "(${
                        SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(track.trackTimeMillis.toLong())
                    })\n"
                )
            }
        }
        Log.d("MessageResult", messageResult)
        return messageResult
    }
}