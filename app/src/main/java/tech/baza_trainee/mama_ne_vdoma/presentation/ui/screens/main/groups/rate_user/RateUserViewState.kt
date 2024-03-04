package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.rate_user

import android.net.Uri
import androidx.compose.runtime.Immutable

@Immutable
data class RateUserViewState(
    val name: String = "Name",
    val avatar: Uri = Uri.EMPTY,
    val rating: Float = 5.0f,
    val reviews: Int = 0,
    val currentRating: Int = 0,
    val note: String = "",
    val isLoading: Boolean = false
)
