package com.practicum.playlistmarker.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmarker.media_library.data.db.AppDatabase
import com.practicum.playlistmarker.search.data.NetworkClient
import com.practicum.playlistmarker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmarker.search.data.network.iTunesApi
import com.practicum.playlistmarker.search.data.repository.LocalSearchHistoryStorage
import com.practicum.playlistmarker.settings.data.LocalStorageThemeApp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val EDIT_THEME = "key_for_edit_theme"
private const val TRACKS_LIST_KEY = "key_for_tracks_list"
val dataModule = module {

    single<iTunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
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
            .create(iTunesApi::class.java)
    }

    single(named(TRACKS_LIST_KEY)) {
        androidContext().getSharedPreferences(TRACKS_LIST_KEY, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<LocalSearchHistoryStorage> {
        val sharedTracklist: SharedPreferences by inject(qualifier = named(TRACKS_LIST_KEY))
        LocalSearchHistoryStorage(sharedTracklist, get(), get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    single(named(EDIT_THEME)) {
        androidContext().getSharedPreferences(EDIT_THEME, Context.MODE_PRIVATE)
    }

    single<LocalStorageThemeApp> {
        val sharedTheme: SharedPreferences by inject(qualifier = named(EDIT_THEME))
        LocalStorageThemeApp(sharedTheme)
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
}