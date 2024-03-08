package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.chat

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupChatModel

@Immutable
data class ChatsViewState(
    val groupChats: SnapshotStateMap<String, GroupChatModel> = mutableStateMapOf(),
    val myId: String = "",
    val selectedChat: String = "",
    val userMessage: String = "",
    val isLoading: Boolean = false
)
