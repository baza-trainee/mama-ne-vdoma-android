package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.AdminJoinRequestCard
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.MyRequestCard
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.customTabIndicatorOffset
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    screenState: State<NotificationsViewState> = mutableStateOf(NotificationsViewState()),
    uiState: State<NotificationsUiState> = mutableStateOf(NotificationsUiState.Idle),
    handleEvent: (NotificationsEvent) -> Unit = {}
) {
    BackHandler { handleEvent(NotificationsEvent.OnBack) }

    val context = LocalContext.current

    var showAcceptDialog by rememberSaveable { mutableStateOf(false) }

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

    var showCancelDialog by rememberSaveable { mutableStateOf(false) }
    var showDeclineDialog by rememberSaveable { mutableStateOf(false) }
    var dialogData by rememberSaveable { mutableStateOf(Triple("", "", "")) }

    Column {
        val density = LocalDensity.current
        val tabs = listOf("Мої запити", "Вхідні запити")
        val tabWidths = remember {
            val tabWidthStateList = mutableStateListOf<Dp>()
            repeat(tabs.size) {
                tabWidthStateList.add(0.dp)
            }
            tabWidthStateList
        }
        val pagerState = rememberPagerState(pageCount = { tabs.size })
        val scope = rememberCoroutineScope()

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
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                ) {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 8.dp),
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
                0 -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        itemsIndexed(screenState.value.myJoinRequests) { index, request ->
                            if (index != 0)
                                Spacer(modifier = Modifier.height(8.dp))

                            MyRequestCard(
                                request = request,
                                onCancel = {
                                    dialogData = Triple(request.group.id, request.child.childId, request.group.name)
                                    showCancelDialog = true
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
                            if (index != 0)
                                Spacer(modifier = Modifier.height(8.dp))

                            AdminJoinRequestCard(
                                request = request,
                                onAccept = {
                                    dialogData = Triple(request.group.id, request.child.childId, request.group.name)
                                    handleEvent(NotificationsEvent.AcceptUser(request.group.id, request.child.childId))
                                },
                                onDecline = {
                                    dialogData = Triple(request.group.id, request.child.childId, request.group.name)
                                    showDeclineDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }

        if (showAcceptDialog) {
            AlertDialog(onDismissRequest = { showAcceptDialog = false }) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "alert",
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        text = "Ви погодили запит на приєднання до групи \"${dialogData.third}\"",
                        fontSize = 14.sp,
                        fontFamily = redHatDisplayFontFamily,
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.End)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                showAcceptDialog = false
                            },
                        text = "Закрити",
                        fontSize = 16.sp,
                        fontFamily = redHatDisplayFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (showDeclineDialog) {
            AlertDialog(onDismissRequest = { showDeclineDialog = false }) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "alert",
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        text = "Ви впевнені, що хочете відхилити запит на приєднання до групи \"${dialogData.third}\"?",
                        fontSize = 14.sp,
                        fontFamily = redHatDisplayFontFamily,
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(0.5f)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    showDeclineDialog = false
                                    handleEvent(
                                        NotificationsEvent.DeclineUser(
                                            dialogData.first,
                                            dialogData.second
                                        )
                                    )
                                },
                            text = "Відхилити",
                            fontSize = 16.sp,
                            fontFamily = redHatDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            modifier = Modifier
                                .weight(0.5f)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    showDeclineDialog = false
                                    handleEvent(
                                        NotificationsEvent.AcceptUser(
                                            dialogData.first,
                                            dialogData.second
                                        )
                                    )
                                },
                            text = "Погодити",
                            fontSize = 16.sp,
                            fontFamily = redHatDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        if (showCancelDialog) {
            AlertDialog(onDismissRequest = { showCancelDialog = false }) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "alert",
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        text = "Ви впевнені, що хочете скасувати запит на приєднання до групи \"${dialogData.third}\"?",
                        fontSize = 14.sp,
                        fontFamily = redHatDisplayFontFamily,
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(0.5f)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { showCancelDialog = false },
                            text = "Ні",
                            fontSize = 16.sp,
                            fontFamily = redHatDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            modifier = Modifier
                                .weight(0.5f)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    showCancelDialog = false
                                    handleEvent(
                                        NotificationsEvent.CancelRequest(
                                            dialogData.first,
                                            dialogData.second
                                        )
                                    )
                                },
                            text = "Так, скасувати",
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

        if (screenState.value.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun NotificationScreenPreview() {
    NotificationScreen()
}