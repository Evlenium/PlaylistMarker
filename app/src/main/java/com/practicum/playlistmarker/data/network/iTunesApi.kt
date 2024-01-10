package com.practicum.playlistmarker.data.network

import com.practicum.playlistmarker.data.dto.TracksResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TracksResponse>
}

private const val iTunesSearchBaseUrl = "https://itunes.apple.com"
private val retrofit = Retrofit.Builder()
    .baseUrl(iTunesSearchBaseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .client(
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()
    )
    .build()

val iTunesService: iTunesApi = retrofit.create(iTunesApi::class.java)