package com.practicum.playlistmarker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val buttonBackToMainFromSettings =
            findViewById<ImageButton>(R.id.buttonBackToMainFromSettings)
        buttonBackToMainFromSettings.setOnClickListener {
            finish()
        }

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
//            val email = resources.getString(R.string.support_mail)
//            val subject = resources.getString(R.string.support_subject)
//            val body = resources.getString(R.string.support_body)
//            ShareCompat.IntentBuilder.from(this)
//                .setType("message/rfc822")
//                .addEmailTo(email)
//                .setSubject(subject)
//                .setText(body)
//                .startChooser()

//            val address = resources.getString(R.string.support_mail)
//            val subject = resources.getString(R.string.support_subject)
//            val body = resources.getString(R.string.support_body)
//            val intent = Intent(Intent.ACTION_SENDTO)
//            intent.data = Uri.parse("mailto:$address")
//            //intent.putExtra(Intent.EXTRA_EMAIL, address)
//            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
//            intent.putExtra(Intent.EXTRA_TEXT, body)
//            startActivity(intent)

            val email = resources.getString(R.string.support_mail)
            val subject = resources.getString(R.string.support_subject)
            val body = resources.getString(R.string.support_body)
            val uri: Uri = Uri.parse("mailto:")
                .buildUpon()
                .appendQueryParameter("to",email)
                .appendQueryParameter("subject",subject)
                .appendQueryParameter("body", body)
                .build()
            val emailIntent = Intent(Intent.ACTION_SENDTO,uri)
            startActivity(Intent.createChooser(emailIntent,"subject"))
        }

        val buttonTermsOfUse = findViewById<ImageButton>(R.id.buttonTermsOfUse)
        buttonTermsOfUse.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.YP_offer_url)))
            startActivity(browserIntent)
        }
    }
}