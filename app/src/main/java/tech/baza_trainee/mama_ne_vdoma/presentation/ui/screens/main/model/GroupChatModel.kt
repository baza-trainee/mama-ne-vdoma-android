package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model

import android.net.Uri
import tech.baza_trainee.mama_ne_vdoma.domain.socket.ChatMessage

data class GroupChatModel(
    val id: String = "",
    val name: String = "",
    val avatar: Uri = Uri.EMPTY,
    val messages: List<ChatMessage> = emptyList(),
    val members: Set<ParentInChatModel> = emptySet(),
    val unread: Int = 0
)
