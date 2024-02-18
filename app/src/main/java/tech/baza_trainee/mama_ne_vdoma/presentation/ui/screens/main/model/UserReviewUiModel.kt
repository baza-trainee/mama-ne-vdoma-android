package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model

import android.net.Uri
import androidx.compose.runtime.Stable

@Stable
data class UserReviewUiModel(
    val name: String = "",
    val rating: Int = 5,
    val note: String = "",
    val avatar: Uri = Uri.EMPTY,
    val timestamp: String = ""
)
