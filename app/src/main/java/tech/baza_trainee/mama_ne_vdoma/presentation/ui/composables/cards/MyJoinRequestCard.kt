package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.Rating
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.JoinRequestUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.LogoutButtonColor
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.LogoutButtonTextColor
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_11_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_16_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_24_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_40_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_4_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp

@Composable
@Preview
fun MyRequestCard(
    modifier: Modifier = Modifier,
    request: JoinRequestUiModel = JoinRequestUiModel(),
    onCancel: () -> Unit = {}
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
                            text = stringResource(id = R.string.join_request_sent, request.group.name),
                            fontSize = font_size_14_sp,
                            fontFamily = redHatDisplayFontFamily
                        )
                    }
                }

                Text(
                    modifier = Modifier
                        .padding(top = size_8_dp)
                        .padding(horizontal = size_8_dp)
                        .align(Alignment.End)
                        .clickable { onCancel() },
                    text = stringResource(id = R.string.cancel_join_request),
                    fontSize = font_size_16_sp,
                    fontFamily = redHatDisplayFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(size_8_dp)
                    )
                    .padding(top = size_8_dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
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
                            .clip(RoundedCornerShape(size_4_dp)),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(request.group.avatar)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.no_photo),
                        contentDescription = "group_avatar",
                        contentScale = ContentScale.FillWidth
                    )

                    Rating(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(all = size_8_dp),
                        rating = request.group.rating
                    )
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = size_8_dp),
                    text = stringResource(id = R.string.format_group_id, request.group.id),
                    fontSize = font_size_11_sp,
                    fontFamily = redHatDisplayFontFamily
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = size_4_dp),
                    text = request.group.name,
                    fontSize = font_size_16_sp,
                    fontFamily = redHatDisplayFontFamily,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = size_4_dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        modifier = Modifier
                            .padding(end = size_4_dp),
                        painter = painterResource(id = R.drawable.ic_group_children),
                        contentDescription = "children_age",
                        contentScale = ContentScale.Inside
                    )

                    Text(
                        text = stringResource(id = R.string.format_age, request.group.ages),
                        fontSize = font_size_14_sp,
                        fontFamily = redHatDisplayFontFamily
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = size_4_dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        modifier = Modifier.padding(end = size_4_dp),
                        painter = painterResource(id = R.drawable.ic_group_location),
                        contentDescription = "group_location",
                        contentScale = ContentScale.Inside
                    )

                    Text(
                        text = request.group.address,
                        fontSize = font_size_14_sp,
                        fontFamily = redHatDisplayFontFamily
                    )
                }

                Spacer(modifier = Modifier.height(size_8_dp))

                request.group.members.forEachIndexed { index, member ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .padding(end = size_8_dp)
                                .height(size_24_dp)
                                .width(size_24_dp)
                                .clip(CircleShape),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(member.avatar)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(id = R.drawable.no_photo),
                            contentDescription = "member",
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = member.name,
                            fontSize = font_size_14_sp,
                            fontFamily = redHatDisplayFontFamily
                        )
                    }

                    if (index != request.group.members.size - 1)
                        Spacer(modifier = Modifier.height(size_4_dp))
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = size_8_dp)
                        .height(size_48_dp),
                    onClick = { onCancel() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LogoutButtonColor,
                        contentColor = LogoutButtonTextColor
                    )
                ) {
                    ButtonText(
                        text = stringResource(id = R.string.cancel_join_request)
                    )
                }
            }
        }
    }
}