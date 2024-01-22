package com.practicum.playlistmarker.sharing.data

import android.content.Context
import com.practicum.playlistmarker.R
import com.practicum.playlistmarker.sharing.domain.model.EmailData

class ResourceProvider(val context: Context) {
    fun getShareAppLink(): String {
        return context.getString(R.string.button_share_application_text)
    }

    fun getSupportEmailData(): EmailData {
        return EmailData(
            context.getString(R.string.support_mail),
            context.getString(R.string.support_subject),
            context.getString(R.string.support_body),
        )
    }

    fun getTermsLink(): String {
        return context.getString(R.string.yandex_practicum_offer_url)
    }

}