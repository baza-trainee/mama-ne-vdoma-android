package tech.baza_trainee.mama_ne_vdoma.domain.preferences

import android.net.Uri

data class UserPreferences(
    val id: String,
    val avatar: String,
    val avatarUri: Uri,
    val name: String,
    val code: String,
    val phone: String,
    val email: String,
    val address: String,
    val radius: Int,
    val latitude: Double,
    val longitude: Double,
    val sendEmail: Boolean,
    val isUserProfileFilled: Boolean,
    val isChildrenDataProvided: Boolean,
    val currentChild: String,
    val myJoinRequests: Int,
    val adminJoinRequests: Int,
    val login: String,
    val authToken: String
)


