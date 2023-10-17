package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host

import android.net.Uri

data class HostViewState(
    val currentPage: Int = 0,
    val notifications: Int = 0,
    val avatar: Uri = Uri.EMPTY,
    val isLoading: Boolean = false
)
