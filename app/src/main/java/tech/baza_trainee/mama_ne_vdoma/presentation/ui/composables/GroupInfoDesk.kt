package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.GroupMember
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper

@Composable
@Preview
fun GroupInfoDesk(
    modifier: Modifier = Modifier,
    groupAvatar: Bitmap = BitmapHelper.DEFAULT_BITMAP,
    groupId: String = "Group ID",
    title: String = "Group title",
    rating: String = "5.0",
    age: String = "Age",
    location: String = "Location",
    groupDesc: String = "Це місце, де ви можете знайти підтримку та друзів, які розуміють ваші потреби. Ми підтримуємо один одного у важких ситуаціях та ділимося успіхами. Приєднуйтеся до нас, щоб знайти спільноту і ресурси, необхідні для розвитку вашоєї кар'єри та забезпечення добробуту дітей.",
    members: List<GroupMember> = mutableListOf<GroupMember>().apply {
        repeat(10) {
            add(GroupMember())
        }
    }
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 2.dp, top = 4.dp)
                .height(88.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(4.dp)),
                bitmap = groupAvatar.asImageBitmap(),
                contentDescription = "group_avatar",
                contentScale = ContentScale.FillBounds
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
                    text = rating,
                    fontSize = 14.sp,
                    fontFamily = redHatDisplayFontFamily
                )
            }
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 4.dp),
            text = groupId,
            fontSize = 11.sp,
            fontFamily = redHatDisplayFontFamily
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 4.dp),
            text = title,
            fontSize = 16.sp,
            fontFamily = redHatDisplayFontFamily,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 4.dp),
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
                text = age,
                fontSize = 14.sp,
                fontFamily = redHatDisplayFontFamily
            )
        }

        var toggleMoreInfo by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            members.forEachIndexed { index, member ->
                if (index < 3) {
                    Image(
                        modifier = Modifier
                            .padding(end = 2.dp)
                            .height(24.dp)
                            .width(24.dp)
                            .clip(CircleShape),
                        bitmap = member.avatar.asImageBitmap(),
                        contentDescription = "member",
                        contentScale = ContentScale.Fit
                    )
                }
            }
            if (members.size > 3)
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
                        text = "+${members.size - 3}",
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

        if (toggleMoreInfo) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        modifier = Modifier
                            .padding(end = 4.dp),
                        painter = painterResource(id = R.drawable.ic_group_location),
                        contentDescription = "group_location",
                        contentScale = ContentScale.Inside
                    )
                    Text(
                        text = location,
                        fontSize = 14.sp,
                        fontFamily = redHatDisplayFontFamily
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    text = groupDesc,
                    fontSize = 11.sp,
                    fontFamily = redHatDisplayFontFamily
                )

                members.forEach {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .height(24.dp)
                                .width(24.dp)
                                .clip(CircleShape),
                            bitmap = it.avatar.asImageBitmap(),
                            contentDescription = "member",
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = it.name,
                            fontSize = 14.sp,
                            fontFamily = redHatDisplayFontFamily
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_mail),
                                contentDescription = "email",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_phone),
                                contentDescription = "phone",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 4.dp),
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
                checked = false,
                onCheckedChange = {}
            )
        }
    }
}