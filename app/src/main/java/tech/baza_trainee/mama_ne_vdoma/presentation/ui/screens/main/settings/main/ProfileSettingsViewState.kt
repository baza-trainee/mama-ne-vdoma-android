package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.main

import android.net.Uri
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity

data class ProfileSettingsViewState(
    val name: String = "Name",
    val email: String = "email",
    val code: String = "+380",
    val phone: String = "677777777",
    val address: String = "Location",
    val avatar: Uri = Uri.EMPTY,
    val children: List<ChildEntity> = emptyList(),
    val sendEmails: Boolean = true,
    val isLoading: Boolean = false
)
