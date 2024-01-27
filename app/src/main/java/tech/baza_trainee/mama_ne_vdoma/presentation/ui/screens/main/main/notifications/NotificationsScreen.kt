package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.domain.model.MessageType
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.AdminJoinRequestCard
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.MyRequestCard
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.customTabIndicatorOffset
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications.dialogs.AcceptRequestDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications.dialogs.CancelRequestDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications.dialogs.DeclineRequestDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications.items.AcceptedItem
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications.items.AdminItem
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications.items.JoinedItem
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications.items.KickedItem
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications.items.RejectedItem
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.JoinRequestUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.NotificationsUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

private const val MY_REQUESTS = 0
private const val ADMIN_REQUESTS = 1
private const val NOTIFICATIONS = 2

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotificationScreen(
    screenState: NotificationsViewState,
    uiState: State<NotificationsUiState>,
    handleEvent: (NotificationsEvent) -> Unit
) {
    BackHandler { handleEvent(NotificationsEvent.OnBack) }

    val context = LocalContext.current

    var showAcceptDialog by rememberSaveable { mutableStateOf(false) }

    val tabs = listOf("Мої запити", "Вхідні запити", "Сповіщення")
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabs.size })
    var currentPage by rememberSaveable { mutableIntStateOf(0) }

    when (val state = uiState.value) {
        NotificationsUiState.Idle -> Unit
        is NotificationsUiState.OnError -> {
            if (state.error.isNotBlank()) Toast.makeText(
                context,
                state.error,
                Toast.LENGTH_LONG
            ).show()
            handleEvent(NotificationsEvent.ResetUiState)
        }

        NotificationsUiState.OnAccepted -> {
            showAcceptDialog = true
            handleEvent(NotificationsEvent.ResetUiState)
        }
    }

    LaunchedEffect(key1 = currentPage) {
        pagerState.animateScrollToPage(currentPage)
    }

    var showCancelDialog by rememberSaveable { mutableStateOf(false) }
    var showDeclineDialog by rememberSaveable { mutableStateOf(false) }
    var dialogData by rememberSaveable { mutableStateOf(Triple("", "", "")) }

    Column {
        val density = LocalDensity.current
        val tabWidths = remember {
            val tabWidthStateList = mutableStateListOf<Dp>()
            repeat(tabs.size) {
                tabWidthStateList.add(0.dp)
            }
            tabWidthStateList
        }

        TabRow(
            modifier = Modifier.height(52.dp),
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            indicator = { tabPositions ->
                Box(
                    modifier = Modifier
                        .customTabIndicatorOffset(
                            currentTabPosition = tabPositions[pagerState.currentPage],
                            tabWidth = tabWidths[pagerState.currentPage]
                        )
                        .height(4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                        )
                )
            }
        ) {
            tabs.forEachIndexed { index, text ->
                Tab(
                    modifier = Modifier.padding(4.dp),
                    selected = pagerState.currentPage == index,
                    onClick = { currentPage = index }
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = text,
                        fontSize = 14.sp,
                        fontFamily = redHatDisplayFontFamily,
                        onTextLayout = {
                            tabWidths[index] =
                                with(density) { it.size.width.toDp() }
                        },
                        textAlign = TextAlign.Center
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
                MY_REQUESTS -> MyRequests(
                    requests = screenState.myJoinRequests,
                    onCancel = {
                        dialogData = Triple(
                            it.group.id,
                            it.child.childId,
                            it.group.name
                        )
                        showCancelDialog = true
                    }
                )

                ADMIN_REQUESTS -> AdminRequests(
                    requests = screenState.adminJoinRequests,
                    onDecline = {
                        dialogData = Triple(
                            it.group.id,
                            it.child.childId,
                            it.group.name
                        )
                        showDeclineDialog = true
                    },
                    onAccept = {
                        dialogData = Triple(
                            it.group.id,
                            it.child.childId,
                            it.group.name
                        )
                        handleEvent(
                            NotificationsEvent.AcceptUser(
                                it.group.id,
                                it.child.childId
                            )
                        )
                    }
                )

                NOTIFICATIONS -> Notifications(
                    notifications = screenState.notifications,
                    handleEvent = handleEvent,
                    goToAdminRequests = { currentPage = ADMIN_REQUESTS }
                )
            }
        }

        if (showAcceptDialog)
            AcceptRequestDialog(
                groupName = dialogData.third,
                onDismiss = { showAcceptDialog = false }
            )

        if (showDeclineDialog)
            DeclineRequestDialog(
                groupName = dialogData.third,
                onDismiss = { showDeclineDialog = false },
                onDecline = {
                    showDeclineDialog = false
                    handleEvent(
                        NotificationsEvent.DeclineUser(
                            dialogData.first,
                            dialogData.second
                        )
                    )
                },
                onAccept = {
                    showDeclineDialog = false
                    handleEvent(
                        NotificationsEvent.AcceptUser(
                            dialogData.first,
                            dialogData.second
                        )
                    )
                }
            )

        if (showCancelDialog)
            CancelRequestDialog(
                groupName = dialogData.third,
                onDismiss = { showCancelDialog = false },
                onCancel = {
                    showCancelDialog = false
                    handleEvent(
                        NotificationsEvent.CancelRequest(
                            dialogData.first,
                            dialogData.second
                        )
                    )
                }
            )

        if (screenState.isLoading) LoadingIndicator()
    }
}

@Composable
private fun Notifications(
    notifications: List<NotificationsUiModel>,
    handleEvent: (NotificationsEvent) -> Unit,
    goToAdminRequests: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        itemsIndexed(notifications) { index, notification ->
            if (index != 0)
                Spacer(modifier = Modifier.height(8.dp))

            when(notification.type) {
                MessageType.JOIN.type ->
                    JoinedItem(group = notification.group, goToAdminRequests)
                MessageType.ACCEPT.type ->
                    AcceptedItem(group = notification.group, handleEvent)
                MessageType.REJECT.type ->
                    RejectedItem(group = notification.group, handleEvent)
                MessageType.KICK.type ->
                    KickedItem(group = notification.group, handleEvent)
                MessageType.MAKE_ADMIN.type ->
                    AdminItem(group = notification.group, handleEvent)

                else -> Unit
            }
        }

        if (notifications.isNotEmpty()) {
            item("delete_all") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        modifier = Modifier
                            .clickable {
                                handleEvent(NotificationsEvent.ClearNotifications)
                            },
                        text = "Видалити всі сповіщення",
                        fontSize = 16.sp,
                        fontFamily = redHatDisplayFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminRequests(
    requests: List<JoinRequestUiModel>,
    onAccept: (JoinRequestUiModel) -> Unit,
    onDecline: (JoinRequestUiModel) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        itemsIndexed(requests) { index, request ->
            if (index != 0)
                Spacer(modifier = Modifier.height(8.dp))

            AdminJoinRequestCard(
                request = request,
                onAccept = { onAccept(request) },
                onDecline = { onDecline(request) }
            )
        }
    }
}

@Composable
private fun MyRequests(
    requests: List<JoinRequestUiModel>,
    onCancel: (JoinRequestUiModel) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        itemsIndexed(requests) { index, request ->
            if (index != 0)
                Spacer(modifier = Modifier.height(8.dp))

            MyRequestCard(
                request = request,
                onCancel = { onCancel(request) }
            )
        }
    }
}

@Composable
@Preview
fun NotificationScreenPreview() {
    NotificationScreen(
        screenState = NotificationsViewState(),
        uiState = remember { mutableStateOf(NotificationsUiState.Idle) },
        handleEvent = {}
    )
}