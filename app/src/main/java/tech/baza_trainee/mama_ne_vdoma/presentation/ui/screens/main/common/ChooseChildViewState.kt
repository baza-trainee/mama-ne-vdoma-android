package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common

import android.net.Uri
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity

data class ChooseChildViewState(
    val children: List<ChildEntity> = emptyList(),
    val avatar: Uri = Uri.EMPTY,
    val isLoading: Boolean = false
)
