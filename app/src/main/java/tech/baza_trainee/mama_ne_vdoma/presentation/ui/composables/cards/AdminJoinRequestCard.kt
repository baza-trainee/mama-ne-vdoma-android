package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.JoinRequestUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.LogoutButtonColor
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.LogoutButtonTextColor
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_16_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_20_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_120_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_1_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_24_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_2_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_40_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_4_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun AdminJoinRequestCard(
    modifier: Modifier = Modifier,
    request: JoinRequestUiModel = JoinRequestUiModel(),
    onAccept: () -> Unit = {},
    onDecline: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(size_8_dp)
            )
            .padding(all = size_16_dp),
        verticalArrangement = Arrangement.Top
    ) {
        var toggleMoreInfo by rememberSaveable { mutableStateOf(false) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.join_request),
                fontSize = font_size_16_sp,
                fontFamily = redHatDisplayFontFamily,
                fontWeight = FontWeight.SemiBold
            )

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

        if (!toggleMoreInfo) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .height(size_40_dp)
                            .width(size_40_dp)
                            .clip(CircleShape),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(request.parentAvatar)
                            .placeholder(R.drawable.ic_user_no_photo)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.ic_user_no_photo),
                        fallback = painterResource(id = R.drawable.ic_user_no_photo),
                        contentDescription = "avatar",
                        contentScale = ContentScale.FillBounds
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = size_16_dp)
                                .padding(bottom = size_4_dp),
                            text = request.parentName,
                            fontSize = font_size_16_sp,
                            fontFamily = redHatDisplayFontFamily
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = size_16_dp)
                                .padding(bottom = size_4_dp),
                            text = stringResource(id = R.string.join_request_info, request.group.name),
                            fontSize = font_size_14_sp,
                            fontFamily = redHatDisplayFontFamily
                        )
                    }
                }

                Spacer(modifier = Modifier.height(size_24_dp))

                Row(
                    modifier = Modifier
                        .padding(horizontal = size_16_dp)
                        .padding(bottom = size_16_dp)
                        .align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = size_8_dp)
                            .clickable {
                                onDecline()
                            },
                        text = stringResource(id = R.string.action_refuse),
                        fontSize = font_size_16_sp,
                        fontFamily = redHatDisplayFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        modifier = Modifier
                            .padding(horizontal = size_8_dp)
                            .clickable {
                                onAccept()
                            },
                        text = stringResource(id = R.string.action_approve),
                        fontSize = font_size_16_sp,
                        fontFamily = redHatDisplayFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(size_8_dp)
                    )
                    .padding(size_8_dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(request.parentAvatar)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.no_photo),
                    contentDescription = "avatar",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .width(size_120_dp)
                        .height(size_120_dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(size_4_dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = request.parentName,
                    fontSize = font_size_20_sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = Modifier.height(size_4_dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = request.parentPhone,
                    fontSize = font_size_16_sp,
                    textAlign = TextAlign.Center,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = Modifier.height(size_2_dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = request.parentEmail,
                    fontSize = font_size_16_sp,
                    textAlign = TextAlign.Center,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = Modifier.height(size_2_dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .basicMarquee(),
                    text = request.parentAddress,
                    fontSize = font_size_16_sp,
                    textAlign = TextAlign.Center,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = Modifier.height(size_8_dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = size_4_dp)
                        .height(size_1_dp)
                        .background(
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = RectangleShape
                        )
                )

                Spacer(modifier = Modifier.height(size_8_dp))

                ChildInfoDesk(
                    child = request.child,
                    canEdit = false,
                    canDelete = false,
                )

                Spacer(modifier = Modifier.height(size_16_dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(size_48_dp),
                    onClick = onAccept
                ) {
                    ButtonText(
                        text = stringResource(id = R.string.approve_request)
                    )
                }

                Spacer(modifier = Modifier.height(size_8_dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(size_48_dp),
                    onClick = onDecline,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LogoutButtonColor,
                        contentColor = LogoutButtonTextColor
                    )
                ) {
                    ButtonText(
                        text = stringResource(id = R.string.refuse_request)
                    )
                }
            }
        }
    }
}