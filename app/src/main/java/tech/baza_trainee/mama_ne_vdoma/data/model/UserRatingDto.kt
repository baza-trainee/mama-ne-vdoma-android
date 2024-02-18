package tech.baza_trainee.mama_ne_vdoma.data.model

import com.google.gson.annotations.SerializedName

data class UserRatingDto(
    @SerializedName("grade")
    val rating: Float,
    val message: String,
    @SerializedName("fromId")
    val reviewer: String,
    @SerializedName("toId")
    val receiver: String,
    val timestamp: String? = null
)
