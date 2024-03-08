package tech.baza_trainee.mama_ne_vdoma.domain.socket

import com.google.gson.annotations.SerializedName

data class ChatMessage(
    @SerializedName("_id") val id: String = "",
    val userId: String = "",
    val chatId: String = "",
    val message: String = "",
    val createdAt: String = ""
)