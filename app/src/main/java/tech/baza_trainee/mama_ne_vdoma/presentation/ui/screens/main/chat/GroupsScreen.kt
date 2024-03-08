package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import coil.request.ImageRequest
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupChatModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_11_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_12_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_16_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_100_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_24_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_4_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.getMessageDate

@Composable
fun GroupsScreen(
    screenState: ChatsViewState,
    handleEvent: (ChatsScreenEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(size_4_dp)
    ) {
        items(screenState.groupChats.values.toList()) {
            GroupChatDesk(
                groupChat = it,
                handleEvent = handleEvent
            )
        }
    }
}

@Composable
private fun GroupChatDesk(
    groupChat: GroupChatModel,
    handleEvent: (ChatsScreenEvent) -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(size_8_dp)
            )
            .padding(all = size_16_dp)
            .clickable {
                handleEvent(ChatsScreenEvent.GoToChat(groupChat.id))
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(size_100_dp)
                .clip(RoundedCornerShape(size_4_dp)),
            model = ImageRequest.Builder(LocalContext.current)
                .data(groupChat.avatar)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.no_photo),
            contentDescription = "group_avatar",
            contentScale = ContentScale.FillWidth
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = size_8_dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = groupChat.name,
                    fontSize = font_size_16_sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    modifier = Modifier.padding(start = size_8_dp),
                    text = groupChat.messages
                        .lastOrNull()
                        ?.createdAt
                        .orEmpty()
                        .getMessageDate(context),
                    fontSize = font_size_12_sp
                )
            }

            Text(
                text = groupChat.messages
                    .lastOrNull()
                    ?.message
                    .orEmpty(),
                fontSize = font_size_14_sp
            )
        }

        if (groupChat.unread > 0) {
            Badge(
                modifier = Modifier.size(size_24_dp)
            ) {
                Text(
                    text = groupChat.unread.toString(),
                    fontSize = font_size_11_sp
                )
            }
        }
    }
}
