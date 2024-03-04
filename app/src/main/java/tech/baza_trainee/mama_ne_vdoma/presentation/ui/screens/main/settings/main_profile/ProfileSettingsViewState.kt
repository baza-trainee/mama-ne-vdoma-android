package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.main_profile

import android.net.Uri
import androidx.compose.runtime.Immutable
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity

@Immutable
data class ProfileSettingsViewState(
    val name: String = "Name",
    val email: String = "email",
    val code: String = "+380",
    val phone: String = "677777777",
    val address: String = "Location",
    val avatar: Uri = Uri.EMPTY,
    val children: List<ChildEntity> = emptyList(),
    val sendEmails: Boolean = true,
    val isPolicyChecked: Boolean = true,
    val isLoading: Boolean = false
)
