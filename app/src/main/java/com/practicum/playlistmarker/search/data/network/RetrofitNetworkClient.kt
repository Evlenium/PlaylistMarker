package com.practicum.playlistmarker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmarker.search.data.NetworkClient
import com.practicum.playlistmarker.search.data.dto.Response
import com.practicum.playlistmarker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmarker.search.data.dto.TracksSearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val iTunesService: iTunesApi, private val context: Context) :
    NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        if (dto !is TracksSearchRequest) {
            return Response().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO) {
            var response: retrofit2.Response<TracksSearchResponse>? = null
            try {
                response = iTunesService.searchTracks(dto.expression)
                response.body()!!.apply { resultCode = 200 }
            } catch (e: Throwable) {
                if (response!!.code() == 404) {
                    Response().apply { resultCode = 404 }
                } else {
                    Response().apply { resultCode = 500 }
                }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

}