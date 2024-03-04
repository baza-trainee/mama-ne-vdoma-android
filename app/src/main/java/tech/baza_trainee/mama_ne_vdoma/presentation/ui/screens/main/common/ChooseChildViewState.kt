package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common

import android.net.Uri
import androidx.compose.runtime.Immutable
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity

@Immutable
data class ChooseChildViewState(
    val children: List<ChildEntity> = emptyList(),
    val avatar: Uri = Uri.EMPTY,
    val notifications: Int = 0,
    val isLoading: Boolean = false
)
