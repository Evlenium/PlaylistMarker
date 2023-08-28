package com.practicum.playlistmarker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBackToMainFromSettings = findViewById<ImageButton>(R.id.buttonBackToMainFromSettings)
        buttonBackToMainFromSettings.setOnClickListener{
            val buttonBackToMainFromSettingsIntent = Intent(this,MainActivity::class.java)
            startActivity(buttonBackToMainFromSettingsIntent)
        }
    }
}