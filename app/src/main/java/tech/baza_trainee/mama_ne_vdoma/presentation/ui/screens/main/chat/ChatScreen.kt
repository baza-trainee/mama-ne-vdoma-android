package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import coil.request.ImageRequest
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.domain.socket.ChatMessage
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.isScrollingUp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.ParentInChatModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Black
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.ChatMessageBackgroundMine
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.ChatMessageBackgroundOther
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.ChatMessageFieldColor
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Gray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.GrayText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.White
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_11_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_16_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_24_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_28_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_40_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_4_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.formatDate
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.formatTime

@Composable
fun ChatScreen(
    screenState: ChatsViewState,
    handleEvent: (ChatsScreenEvent) -> Unit
) {
    val lazyListState = rememberLazyListState()

    var needToScroll by remember { mutableStateOf(false) }
    var needToScrollByFAB by remember { mutableStateOf(false) }
    val isScrollingUp = lazyListState.isScrollingUp()

    val chatMessages by remember {
        derivedStateOf { screenState.groupChats[screenState.selectedChat]?.messages.orEmpty() }
    }
    val chatMembers by remember {
        derivedStateOf { screenState.groupChats[screenState.selectedChat]?.members.orEmpty() }
    }
    val key by remember {
        derivedStateOf { screenState.groupChats[screenState.selectedChat]?.messages?.get(9)?.id.orEmpty() }
    }

    val firstVisible by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex }
    }

    val visibleItems by remember {
        derivedStateOf { lazyListState.layoutInfo.visibleItemsInfo }
    }

    LaunchedEffect(Unit) {
        lazyListState.scrollToItem(chatMessages.lastIndex)
    }

    LaunchedEffect(chatMessages.size) {
        if (needToScroll) {
            lazyListState.scrollToItem(chatMessages.lastIndex)
            needToScroll = false
        }
    }

    LaunchedEffect(needToScrollByFAB) {
        if (needToScrollByFAB) {
            lazyListState.scrollToItem(chatMessages.lastIndex)
            needToScrollByFAB = false
        }
    }

    LaunchedEffect(visibleItems.lastOrNull()?.index) {
        val isAtEnd = visibleItems.lastOrNull()?.index ==
                lazyListState.layoutInfo.totalItemsCount - 1
        if (isAtEnd) {
            handleEvent(ChatsScreenEvent.SetLastViewed(
                chatMessages.lastOrNull()?.chatId.orEmpty(),
                chatMessages.lastOrNull()?.id.orEmpty()
            ))
        }
    }

    LaunchedEffect(visibleItems.firstOrNull()?.key) {
        val isAtTop = visibleItems.firstOrNull()?.key == key && isScrollingUp
        if (isAtTop)
            handleEvent(ChatsScreenEvent.OnLoadMore)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(size_28_dp),
                    color = ChatMessageFieldColor
                )
                .clip(RoundedCornerShape(size_28_dp))
                .clickable { handleEvent(ChatsScreenEvent.OnBack) }
                .padding(all = size_8_dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )

            Text(
                text = stringResource(id = R.string.go_to_chats),
                fontSize = font_size_16_sp
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(top = size_16_dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(size_4_dp),
                state = lazyListState
            ) {
                chatMessages.groupBy { it.createdAt.formatDate() }.forEach { entry ->
                    item {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = entry.key,
                            textAlign = TextAlign.Center,
                            fontSize = font_size_11_sp,
                            color = GrayText
                        )
                    }

                    items(
                        items = entry.value,
                        key = { it.id }
                    ) { message ->
                        if (message.userId == screenState.myId)
                            MyChatMessageView(message = message)
                        else
                            OtherChatMessageView(
                                message = message,
                                member = chatMembers.find { it.id == message.userId }
                                    ?: ParentInChatModel()
                            )
                    }
                }
            }

            if (firstVisible + visibleItems.size <= chatMessages.lastIndex) {
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = size_4_dp, end = size_4_dp),
                    shape = CircleShape,
                    containerColor = ChatMessageFieldColor,
                    onClick = { needToScrollByFAB = true },
                ) {
                    Icon(
                        modifier = Modifier.rotate(270f),
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_24_dp),
            value = screenState.userMessage,
            onValueChange = { handleEvent(ChatsScreenEvent.SetMessage(it)) },
            trailingIcon = {
                IconButton(
                    onClick = {
                        handleEvent(ChatsScreenEvent.SendMessage)

                        needToScroll = true
                    }
                ) {
                    Icon(
                        modifier = Modifier.rotate(315f),
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = null
                    )
                }
            },
            maxLines = 3,
            shape = RoundedCornerShape(size_28_dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ChatMessageFieldColor,
                unfocusedBorderColor = ChatMessageFieldColor,
                disabledBorderColor = ChatMessageFieldColor,
                errorBorderColor = ChatMessageFieldColor,
                focusedContainerColor = ChatMessageFieldColor,
                unfocusedContainerColor = ChatMessageFieldColor,
                disabledContainerColor = ChatMessageFieldColor,
                errorContainerColor = ChatMessageFieldColor
            )
        )
    }
}

@Composable
private fun MyChatMessageView(
    message: ChatMessage
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(.6f)
                .background(
                    color = ChatMessageBackgroundMine,
                    shape = RoundedCornerShape(
                        topStart = size_8_dp,
                        topEnd = size_8_dp,
                        bottomStart = size_8_dp
                    )
                )
                .padding(all = size_8_dp)
        ) {
            Row {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.Top),
                    text = message.message,
                    fontSize = font_size_14_sp,
                    color = White
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.Bottom),
                    text = message.createdAt.formatTime(),
                    fontSize = font_size_11_sp,
                    color = Gray
                )
            }
        }
    }
}

@Composable
private fun OtherChatMessageView(
    message: ChatMessage,
    member: ParentInChatModel
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        AsyncImage(
            modifier = Modifier
                .size(size_40_dp)
                .clip(CircleShape),
            model = ImageRequest.Builder(LocalContext.current)
                .data(member.avatar)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.no_photo),
            contentDescription = "user_avatar",
            contentScale = ContentScale.FillWidth
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(.6f)
                .background(
                    color = ChatMessageBackgroundOther,
                    shape = RoundedCornerShape(
                        topStart = size_8_dp,
                        topEnd = size_8_dp,
                        bottomEnd = size_8_dp
                    )
                )
                .padding(all = size_8_dp)
        ) {
            Row {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = member.name,
                        fontSize = font_size_16_sp,
                        color = Black,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = message.message,
                        fontSize = font_size_14_sp,
                        color = Black
                    )
                }

                Text(
                    modifier = Modifier
                        .align(Alignment.Bottom),
                    text = message.createdAt.formatTime(),
                    fontSize = font_size_11_sp,
                    color = GrayText
                )
            }
        }
    }
}