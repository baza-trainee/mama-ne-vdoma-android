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
import androidx.compose.runtime.remember
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.JoinRequestUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.LogoutButtonColor
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.LogoutButtonTextColor
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
@Preview
fun MyRequestCard(
    modifier: Modifier = Modifier,
    request: JoinRequestUiModel = JoinRequestUiModel(),
    onDecline: (String, String) -> Unit = {_,_ ->}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        var toggleMoreInfo by remember { mutableStateOf(false) }

        IconButton(
            modifier = Modifier.align(Alignment.End),
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

        if (!toggleMoreInfo) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .height(40.dp)
                            .width(40.dp)
                            .clip(CircleShape),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(request.parentAvatar)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.no_photo),
                        contentDescription = "avatar",
                        contentScale = ContentScale.FillBounds
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 4.dp),
                            text = request.parentName,
                            fontSize = 16.sp,
                            fontFamily = redHatDisplayFontFamily
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 4.dp),
                            text = "Запит на приєднання до групи \"${request.group.name}\"",
                            fontSize = 14.sp,
                            fontFamily = redHatDisplayFontFamily
                        )
                    }
                }

                Text(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .align(Alignment.End)
                        .clickable { },
                    text = "Скасувати запит",
                    fontSize = 16.sp,
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
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp),
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
                            .clip(RoundedCornerShape(4.dp)),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(request.group.avatar)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.no_photo),
                        contentDescription = "group_avatar",
                        contentScale = ContentScale.FillWidth
                    )

                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .height(28.dp)
                            .width(64.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .align(Alignment.TopEnd),
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

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "ID: ${request.group.id}",
                    fontSize = 11.sp,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = request.group.name,
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
                        text = "${request.group.ages} р.",
                        fontSize = 14.sp,
                        fontFamily = redHatDisplayFontFamily
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                request.group.members.forEachIndexed { index, member ->
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
                                .data(member.avatar)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(id = R.drawable.no_photo),
                            contentDescription = "member",
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = member.name,
                            fontSize = 14.sp,
                            fontFamily = redHatDisplayFontFamily
                        )
                    }

                    if (index != request.group.members.size - 1)
                        Spacer(modifier = Modifier.height(4.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LogoutButtonColor,
                        contentColor = LogoutButtonTextColor
                    )
                ) {
                    ButtonText(
                        text = "Скасувати запит"
                    )
                }
            }
        }
    }
}