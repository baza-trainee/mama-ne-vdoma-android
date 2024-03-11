package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.ChatScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.asStateWithLifecycle
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.chat.ChatsScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.chat.ChatsViewModel

fun NavGraphBuilder.chatNavGraph() {
    navigation(
        route = Graphs.HostNested.Chat.route,
        startDestination = ChatScreenRoutes.Chats.route
    ) {
        composable(ChatScreenRoutes.Chats.route) {
            val chatsViewModel: ChatsViewModel = koinNavViewModel()
            ChatsScreen(
                screenState = chatsViewModel.viewState.asStateWithLifecycle(),
                uiState = chatsViewModel.uiState,
                handleEvent = chatsViewModel::handleEvent
            )
        }
    }
}
