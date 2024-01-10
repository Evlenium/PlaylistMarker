package com.practicum.playlistmarker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.practicum.playlistmarker.App
import com.practicum.playlistmarker.App.Companion.EDIT_THEME
import com.practicum.playlistmarker.App.Companion.PRACTICUM_PLAYLISTMARKER_PREFERENCES_THEME
import com.practicum.playlistmarker.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPrefs =
            getSharedPreferences(PRACTICUM_PLAYLISTMARKER_PREFERENCES_THEME, MODE_PRIVATE)

        val toolbarSearch = findViewById<Toolbar>(R.id.toolbarSettings)
        toolbarSearch.setNavigationIcon(R.drawable.bt_arrow_back_mode)
        toolbarSearch.setNavigationOnClickListener { finish() }
        toolbarSearch.setTitleTextAppearance(this, R.style.SecondsActivityMediumTextAppearance)

        val buttonShareApplication = findViewById<ImageButton>(R.id.buttonShareApplication)
        buttonShareApplication.setOnClickListener {
            val shareText = resources.getString(R.string.button_share_application_text)
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            startActivity(sendIntent)
        }

        val buttonSupport = findViewById<ImageButton>(R.id.buttonSupport)
        buttonSupport.setOnClickListener {

            val email = resources.getString(R.string.support_mail)
            val subject = resources.getString(R.string.support_subject)
            val body = resources.getString(R.string.support_body)
            val uri: Uri = Uri.parse("mailto:")
                .buildUpon()
                .appendQueryParameter(resources.getString(R.string.to), email)
                .appendQueryParameter(resources.getString(R.string.subject), subject)
                .appendQueryParameter(resources.getString(R.string.body), body)
                .build()
            val emailIntent = Intent(Intent.ACTION_SENDTO, uri)
            startActivity(Intent.createChooser(emailIntent, (resources.getString(R.string.subject))))
        }

        val buttonTermsOfUse = findViewById<ImageButton>(R.id.buttonTermsOfUse)
        buttonTermsOfUse.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.yandex_practicum_offer_url)))
            startActivity(browserIntent)
        }

        var isChecked = false
        val themeSwitcher = findViewById<SwitchCompat>(R.id.swTheme)
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            isChecked = checked
            (applicationContext as App).switchTheme(checked)
            sharedPrefs.edit().putBoolean(EDIT_THEME, checked).apply()
        }
        themeSwitcher.isChecked =
            sharedPrefs.getBoolean(EDIT_THEME, (applicationContext as App).darkTheme)
        (applicationContext as App).switchTheme(isChecked)
    }
}