package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun HeaderWithToolbar(
    modifier: Modifier = Modifier,
    title: String = "Title",
    avatar: Uri = Uri.EMPTY,
    showNotification: Boolean = true,
    notificationCount: Int = 2,
    onNotificationsClicked: () -> Unit = {},
    onAvatarClicked: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(128.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        ToolbarWithAvatar(
            avatar = avatar,
            showTitle = false,
            showNotification = showNotification,
            notificationCount = notificationCount,
            onNotificationsClicked = onNotificationsClicked,
            onAvatarClicked = onAvatarClicked,
            onBack = onBack
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 2.dp)
                .fillMaxWidth(),
            text = title,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = redHatDisplayFontFamily,
            lineHeight = 28.sp
        )
    }
}