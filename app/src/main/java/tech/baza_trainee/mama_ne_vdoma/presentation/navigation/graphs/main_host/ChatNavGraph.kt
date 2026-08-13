package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.ChatScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.asStateWithLifecycle
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.chat.ChatsScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.chat.ChatsViewModel

fun NavGraphBuilder.chatNavGraph() {
    navigation<Graphs.HostNested.Chat>(
        startDestination = ChatScreenRoutes.Chats
    ) {
        composable<ChatScreenRoutes.Chats> {
            val chatsViewModel: ChatsViewModel = koinViewModel()
            ChatsScreen(
                screenState = chatsViewModel.viewState.asStateWithLifecycle(),
                uiState = chatsViewModel.uiState.asStateWithLifecycle(),
                handleEvent = chatsViewModel::handleEvent
            )
        }
    }
}
