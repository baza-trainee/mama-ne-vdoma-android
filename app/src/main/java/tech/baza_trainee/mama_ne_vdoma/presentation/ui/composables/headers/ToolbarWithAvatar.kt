package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_24_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_32_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_40_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_64_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ToolbarWithAvatar(
    modifier: Modifier = Modifier,
    avatar: Uri = Uri.EMPTY,
    showArrow: Boolean = true,
    showTitle: Boolean = true,
    title: String = "Title",
    showNotification: Boolean = true,
    notificationCount: Int = 18,
    onNotificationsClicked: () -> Unit = {},
    onAvatarClicked: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(size_64_dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showArrow) {
            IconButton(
                modifier = Modifier
                    .padding(horizontal = size_16_dp)
                    .height(size_24_dp)
                    .width(size_24_dp),
                onClick = { onBack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        if (showTitle) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = title,
                textAlign = TextAlign.Center,
                fontFamily = redHatDisplayFontFamily,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )


        if (showNotification) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(size_8_dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .height(size_40_dp)
                        .width(size_40_dp)
                        .clip(CircleShape)
                        .clickable {
                            onNotificationsClicked()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier
                            .height(size_32_dp)
                            .width(size_32_dp),
                        painter = painterResource(id = R.drawable.ic_notification),
                        contentDescription = "notification",
                        contentScale = ContentScale.Fit
                    )
                }
                Badge(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        text = if (notificationCount < 10) notificationCount.toString() else "9+",
                        fontFamily = redHatDisplayFontFamily
                    )
                }
            }
        }

        AsyncImage(
            modifier = Modifier
                .padding(end = size_8_dp)
                .height(size_40_dp)
                .width(size_40_dp)
                .clip(CircleShape)
                .clickable {
                    onAvatarClicked()
                },
            model = ImageRequest.Builder(LocalContext.current)
                .data(avatar)
                .placeholder(R.drawable.ic_user_no_photo_inverse)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.ic_user_no_photo_inverse),
            fallback = painterResource(id = R.drawable.ic_user_no_photo_inverse),
            contentDescription = "avatar",
            contentScale = ContentScale.FillBounds
        )
    }
}