package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.AdminJoinRequestCard
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.MyRequestCard
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    screenState: State<NotificationsViewState> = mutableStateOf(NotificationsViewState()),
    handleEvent: (NotificationsEvent) -> Unit = {}
) {
    BackHandler { handleEvent(NotificationsEvent.OnBack) }

    Column {
        val tabs = listOf("Мої запити", "Вхідні запити")
        val pagerState = rememberPagerState(pageCount = { tabs.size })
        val scope = rememberCoroutineScope()

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            indicator = { tabPositions ->
                SecondaryIndicator(modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])  )
            }
        ) {
            tabs.forEachIndexed { index, text ->
                Tab(
                    modifier = Modifier.padding(4.dp),
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                ) {
                    Text(
                        text = text,
                        fontSize = 14.sp,
                        fontFamily = redHatDisplayFontFamily
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalAlignment = Alignment.Top
        ) { page ->
            when (page) {
                0 -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        itemsIndexed(screenState.value.myJoinRequests) { index, request ->
                            if(index != 0)
                                Spacer(modifier = Modifier.height(8.dp))

                            MyRequestCard(
                                request = request,
                                onDecline = { groupId, childId ->
                                    handleEvent(
                                        NotificationsEvent.DeclineUser(
                                            groupId,
                                            childId
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
                1 -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        itemsIndexed(screenState.value.adminJoinRequests) { index, request ->
                            if(index != 0)
                                Spacer(modifier = Modifier.height(8.dp))

                            AdminJoinRequestCard(
                                request = request,
                                onAccept = { groupId, childId -> handleEvent(NotificationsEvent.AcceptUser(groupId, childId)) },
                                onDecline = { groupId, childId -> handleEvent(NotificationsEvent.DeclineUser(groupId, childId)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun NotificationScreenPreview() {
    NotificationScreen()
}