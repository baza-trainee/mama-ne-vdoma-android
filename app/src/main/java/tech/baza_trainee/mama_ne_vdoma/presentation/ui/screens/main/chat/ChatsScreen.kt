package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

private const val CHATS = 0
private const val CHAT = 1

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatsScreen(
    screenState: ChatsViewState,
    uiState: State<RequestState>,
    handleEvent: (ChatsScreenEvent) -> Unit
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    BackHandler { handleEvent(ChatsScreenEvent.OnBack) }

    LaunchedEffect(screenState.selectedChat) {
        if (screenState.selectedChat.isEmpty())
            pagerState.scrollToPage(CHATS)
        else
            pagerState.scrollToPage(CHAT)
    }

    when (val state = uiState.value) {
        RequestState.Idle -> Unit
        is RequestState.OnError -> {
            context.showToast(state.error)
            handleEvent(ChatsScreenEvent.ResetUiState)
        }
    }

    HorizontalPager(
        modifier = Modifier.padding(vertical = size_16_dp),
        state = pagerState,
        userScrollEnabled = false
    ) {
        when(it) {
            CHATS -> GroupsScreen(
                screenState = screenState,
                handleEvent = handleEvent
            )

            CHAT -> ChatScreen(
                screenState = screenState,
                handleEvent = handleEvent
            )
        }
    }
}