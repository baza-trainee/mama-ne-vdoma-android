package tech.baza_trainee.mama_ne_vdoma.domain.preferences

data class UserPreferences(
    val id: String,
    val avatar: String,
    val name: String,
    val code: String,
    val phone: String,
    val email: String,
    val address: String,
    val radius: Int,
    val latitude: Double,
    val longitude: Double,
    val notificationCount: Int
)


