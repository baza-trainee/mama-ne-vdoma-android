package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp

private const val CHATS = 0
private const val CHAT = 1

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatsScreen(
    screenState: ChatsViewState,
    handleEvent: (ChatsScreenEvent) -> Unit
) {
    BackHandler { handleEvent(ChatsScreenEvent.OnBack) }

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    LaunchedEffect(screenState.selectedChat) {
        if (screenState.selectedChat.isEmpty())
            pagerState.scrollToPage(CHATS)
        else
            pagerState.scrollToPage(CHAT)
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