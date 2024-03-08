package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.CHAT_PAGE

sealed class ChatScreenRoutes(override val route: String, override val title: Int): CommonHostRoute(route, CHAT_PAGE, title) {
    data object Chats: ChatScreenRoutes("chats_screen", title = R.string.title_chat)
}