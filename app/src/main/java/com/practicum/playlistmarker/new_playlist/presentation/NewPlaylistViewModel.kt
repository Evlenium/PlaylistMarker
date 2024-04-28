package com.practicum.playlistmarker.new_playlist.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmarker.media_library.domain.db.api.playlist.PlaylistInteractor
import com.practicum.playlistmarker.media_library.domain.model.playlist.Playlist
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    val uriUUIDLiveData = MutableLiveData<String?>()

    private val playlistLiveData = MutableLiveData<Playlist>()
    fun observePlaylist(): MutableLiveData<Playlist> = playlistLiveData
    fun fillPlaylistUpdateInfo(playlistId: Int) {
        viewModelScope.launch {
            playlistLiveData.postValue(playlistInteractor.getPlaylistById(playlistId = playlistId))
        }
    }

    fun savePlaylist(name: String, description: String?) {
        viewModelScope.launch {
            playlistInteractor.addPlaylist(
                name,
                description,
                uriUUIDLiveData.value
            )
        }
    }

    fun editPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.updatePlaylist(playlist)
        }
    }

    fun saveImageToPrivateStorage(uri: Uri, context: Context) {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val uuid = UUID.randomUUID()
        uriUUIDLiveData.postValue("$uuid.jpg")
        val file = File(filePath, "$uuid.jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

}