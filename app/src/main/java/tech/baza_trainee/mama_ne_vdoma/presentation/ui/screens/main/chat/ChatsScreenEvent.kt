package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.chat

sealed interface ChatsScreenEvent {

    data class GoToChat(val id: String): ChatsScreenEvent

    data class SetMessage(val message: String): ChatsScreenEvent

    data class SetLastViewed(val chatId: String, val messageId: String): ChatsScreenEvent

    data object SendMessage: ChatsScreenEvent

    data object OnBack: ChatsScreenEvent

    data object OnLoadMore: ChatsScreenEvent
}