package com.practicum.playlistmarker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.search)
        buttonSearch.setOnClickListener {
            val buttonSearchIntent = Intent(this, SearchActivity::class.java)
            startActivity(buttonSearchIntent)
        }

        val buttonMediaLibrary = findViewById<Button>(R.id.mediaLibrary)
        buttonMediaLibrary.setOnClickListener {
            val buttonMediaLibraryIntent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(buttonMediaLibraryIntent)
        }

        val buttonSettings = findViewById<Button>(R.id.settings)
        buttonSettings.setOnClickListener {
            val buttonSettingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(buttonSettingsIntent)
        }
    }
}