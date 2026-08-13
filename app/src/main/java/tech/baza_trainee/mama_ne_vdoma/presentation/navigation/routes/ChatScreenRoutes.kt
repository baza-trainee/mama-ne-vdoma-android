package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import kotlinx.serialization.Serializable
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.CHAT_PAGE

sealed interface ChatScreenRoutes : CommonHostRoute {

    override val page: Int get() = CHAT_PAGE
    override val title: Int get() = R.string.title_chat

    @Serializable
    data object Chats : ChatScreenRoutes
}
