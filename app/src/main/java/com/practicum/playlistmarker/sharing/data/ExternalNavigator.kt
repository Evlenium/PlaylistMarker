package com.practicum.playlistmarker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.sharing.domain.model.EmailData

class ExternalNavigator(val context: Context) {
    fun shareLink(link: String) {
        context.startActivity(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plain"
        })
    }

    fun openLink(link: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    fun openEmail(emailData: EmailData) {
        val uri: Uri = Uri.parse("mailto:")
            .buildUpon()
            .appendQueryParameter(context.getString(R.string.to), emailData.email)
            .appendQueryParameter(
                context.getString(R.string.subject),
                emailData.subject
            )
            .appendQueryParameter(context.getString(R.string.body), emailData.body)
            .build()
        val emailIntent = Intent(Intent.ACTION_SENDTO, uri)
        context.startActivity(
            Intent.createChooser(
                emailIntent,
                (context.getString(R.string.subject))
            )
        )
    }
}