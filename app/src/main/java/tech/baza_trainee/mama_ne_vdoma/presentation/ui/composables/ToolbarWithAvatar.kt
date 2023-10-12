package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.BitmapHelper

@Composable
@Preview
fun ToolbarWithAvatar(
    modifier: Modifier = Modifier,
    avatar: Bitmap = BitmapHelper.DEFAULT_BITMAP,
    showArrow: Boolean = true,
    showTitle: Boolean = true,
    title: String = "",
    showNotification: Boolean = true,
    notificationCount: String = "2",
    onBack: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .height(64.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showArrow) {
            IconButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(24.dp)
                    .width(24.dp),
                onClick = { onBack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

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

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .height(32.dp)
                    .width(32.dp),
                painter = painterResource(id = R.drawable.ic_notification),
                contentDescription = "notification",
                contentScale = ContentScale.Fit
            )

            Box(
                modifier = Modifier
                    .height(16.dp)
                    .width(16.dp)
                    .align(Alignment.TopEnd)
                    .background(
                        color = Color.Red,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = notificationCount,
                    textAlign = TextAlign.Center,
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Image(
            modifier = Modifier
                .padding(end = 8.dp)
                .height(32.dp)
                .width(32.dp)
                .clip(CircleShape),
            bitmap = avatar.asImageBitmap(),
            contentDescription = "avatar",
            contentScale = ContentScale.Fit
        )
    }
}