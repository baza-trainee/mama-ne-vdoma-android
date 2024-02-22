package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications.NotificationsEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

@Composable
fun RejectedItem(
    group: GroupUiModel,
    handleEvent: (NotificationsEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(all = 16.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.join_request_declined, group.name, group.id),
            fontSize = 14.sp,
            fontFamily = redHatDisplayFontFamily
        )
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .align(Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        handleEvent(NotificationsEvent.SearchGroup)
                    },
                text = stringResource(id = R.string.search_new_group),
                fontSize = 16.sp,
                fontFamily = redHatDisplayFontFamily,
                fontWeight = FontWeight.Bold,
                color =  MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        handleEvent(NotificationsEvent.GoToMain)
                    },
                text = stringResource(id = R.string.action_go_to_main),
                fontSize = 16.sp,
                fontFamily = redHatDisplayFontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
    }
}