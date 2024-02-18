package tech.baza_trainee.mama_ne_vdoma.domain.model

data class UserRatingDomainModel(
    val rating: Float = 0f,
    val message: String = "",
    val reviewer: String = "",
    val receiver: String = ""
)
