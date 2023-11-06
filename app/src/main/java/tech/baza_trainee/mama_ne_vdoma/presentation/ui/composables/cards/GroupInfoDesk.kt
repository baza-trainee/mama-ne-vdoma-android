package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.LogoutButtonColor
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.LogoutButtonTextColor
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun GroupInfoDesk(
    modifier: Modifier = Modifier,
    group: GroupUiModel = GroupUiModel(),
    currentUserId: String = "",
    onSelect: (String) -> Unit = {},
    onKick: (String, List<String>) -> Unit = {_,_->},
    onLeave: (String) -> Unit = {},
    onSwitchAdmin: (String, String) -> Unit = {_,_->},
    onDelete: (String) -> Unit = {}
) {
    val isAdmin = currentUserId == group.adminId
    val isMyGroup = group.members.map { it.id }.contains(currentUserId)

    var showAdminDialog by rememberSaveable { mutableStateOf(false) }
    var showKickDialog by rememberSaveable { mutableStateOf(false) }
    var adminDialogData by rememberSaveable { mutableStateOf(Triple("", "", "")) }
    var kickDialogData by rememberSaveable { mutableStateOf(Triple("", emptyList<String>(), "")) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(all = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(4.dp)),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(group.avatar)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.no_photo),
                contentDescription = "group_avatar",
                contentScale = ContentScale.FillWidth
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopEnd),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isAdmin) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .height(28.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .width(16.dp)
                                .height(28.dp),
                            painter = painterResource(id = R.drawable.ic_crown),
                            contentDescription = "admin"
                        )
                        Text(
                            text = "Ви адміністратор групи",
                            fontSize = 14.sp,
                            fontFamily = redHatDisplayFontFamily,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .height(28.dp)
                        .width(64.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .padding(end = 4.dp),
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = "rating"
                    )
                    Text(
                        text = "5.0",
                        fontSize = 14.sp,
                        fontFamily = redHatDisplayFontFamily
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "ID: ${group.id}",
            fontSize = 11.sp,
            fontFamily = redHatDisplayFontFamily
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = group.name,
            fontSize = 16.sp,
            fontFamily = redHatDisplayFontFamily,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                modifier = Modifier
                    .padding(end = 4.dp),
                painter = painterResource(id = R.drawable.ic_group_children),
                contentDescription = "children_age",
                contentScale = ContentScale.Inside
            )

            Text(
                text = "${group.ages} р.",
                fontSize = 14.sp,
                fontFamily = redHatDisplayFontFamily
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        var toggleMoreInfo by rememberSaveable { mutableStateOf(false) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            group.members.forEachIndexed { index, member ->
                if (member.id != currentUserId) {
                    if (index < 3) {
                        AsyncImage(
                            modifier = Modifier
                                .padding(end = 2.dp)
                                .height(24.dp)
                                .width(24.dp)
                                .clip(CircleShape),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(member.avatar)
                                .placeholder(R.drawable.ic_user_no_photo)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(id = R.drawable.ic_user_no_photo),
                            fallback = painterResource(id = R.drawable.ic_user_no_photo),
                            contentDescription = "member",
                            contentScale = ContentScale.Fit
                        )
                    } else return@forEachIndexed
                }
            }

            if (group.members.size > 3)
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .width(24.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+${group.members.size - 3}",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 10.sp,
                        fontFamily = redHatDisplayFontFamily
                    )
                }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = { toggleMoreInfo = !toggleMoreInfo }
            ) {
                Icon(
                    painter = painterResource(
                        id = if (toggleMoreInfo)
                            R.drawable.outline_arrow_drop_up_24
                        else
                            R.drawable.outline_arrow_drop_down_24
                    ),
                    contentDescription = "toggle_more",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (toggleMoreInfo) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        modifier = Modifier.padding(end = 4.dp),
                        painter = painterResource(id = R.drawable.ic_group_location),
                        contentDescription = "group_location",
                        contentScale = ContentScale.Inside
                    )

                    Text(
                        text = group.location,
                        fontSize = 14.sp,
                        fontFamily = redHatDisplayFontFamily
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = group.description,
                    fontSize = 11.sp,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = Modifier.height(8.dp))

                group.members.forEach {
                    if (it.id != currentUserId) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .height(24.dp)
                                    .width(24.dp)
                                    .clip(CircleShape),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(it.avatar)
                                    .placeholder(R.drawable.ic_user_no_photo)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.ic_user_no_photo),
                                fallback = painterResource(id = R.drawable.ic_user_no_photo),
                                contentDescription = "member",
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                text = it.name,
                                fontSize = 14.sp,
                                fontFamily = redHatDisplayFontFamily
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            if (isAdmin) {
                                IconButton(
                                    onClick = {
                                        adminDialogData = Triple(group.id, it.id, it.name)
                                        showAdminDialog = true
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_crown),
                                        contentDescription = "make_admin",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        kickDialogData = Triple(group.id, it.children, it.name)
                                        showKickDialog = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Logout,
                                        contentDescription = "kick",
                                        tint = Color.Red
                                    )
                                }
                            }

                            if (isMyGroup) {
                                val context = LocalContext.current
                                val scope = rememberCoroutineScope()

                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${it.email}"))
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            ContextCompat.startActivity(context, intent, null)
                                        }
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_mail),
                                        contentDescription = "email",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${it.phone}"))
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            ContextCompat.startActivity(context, intent, null)
                                        }
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_phone),
                                        contentDescription = "phone",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }

        if (isMyGroup && !isAdmin) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = { onLeave(group.id) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LogoutButtonColor,
                    contentColor = LogoutButtonTextColor
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "exit"
                )
                Spacer(modifier = Modifier.width(4.dp))
                ButtonText(
                    text = "Покинути групу"
                )
            }
        } else if (!isAdmin) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = 4.dp),
                    text = "Приєднатись до групи",
                    fontSize = 11.sp,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = Modifier.weight(1f))

                Checkbox(
                    checked = group.isChecked,
                    onCheckedChange = { onSelect(group.id) }
                )
            }
        }

        if (showAdminDialog) {
            AlertDialog(onDismissRequest = { showAdminDialog = false }) {
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
                        text = "Ви впевнені, що хочете передати адмінастративні права в групі \"${adminDialogData.third}\"?",
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
                                ) { showAdminDialog = false },
                            text = "Ні",
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
                                    showAdminDialog = false
                                    onSwitchAdmin(adminDialogData.first, adminDialogData.second)
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

        if (showKickDialog) {
            AlertDialog(onDismissRequest = { showKickDialog = false }) {
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
                        text = "Ви впевнені, що хочете видалити \"${kickDialogData.third}\" з групи?",
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
                                ) { showKickDialog = false },
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
                                    showKickDialog = false
                                    onKick(kickDialogData.first, kickDialogData.second)
                                },
                            text = "Так, видалити",
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
}