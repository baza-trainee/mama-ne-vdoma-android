package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common

import android.net.Uri
import androidx.compose.runtime.Stable
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity

data class ChooseChildViewState(
    @Stable val children: List<ChildEntity> = emptyList(),
    @Stable val avatar: Uri = Uri.EMPTY,
    val isLoading: Boolean = false
)
