package com.practicum.playlistmarker.sharing.domain.use_case

import com.practicum.playlistmarker.sharing.data.ExternalNavigator
import com.practicum.playlistmarker.sharing.data.ResourceProvider
import com.practicum.playlistmarker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmarker.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val resourceProvider: ResourceProvider,
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    override fun getMessageAddedToPlaylist():String {
        return resourceProvider.getMessageAddedToPlaylist()
    }

    override fun getMessageAddedToPlaylistYet():String {
        return resourceProvider.getMessageAddedToPlaylistYet()
    }

    private fun getShareAppLink(): String {
        return resourceProvider.getShareAppLink()
    }

    private fun getSupportEmailData(): EmailData {
        return resourceProvider.getSupportEmailData()
    }

    private fun getTermsLink(): String {
        return resourceProvider.getTermsLink()
    }
}