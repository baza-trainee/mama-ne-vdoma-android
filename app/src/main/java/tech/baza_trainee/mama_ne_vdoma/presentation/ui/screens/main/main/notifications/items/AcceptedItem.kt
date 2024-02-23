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
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications.NotificationsEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_16_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp

@Composable
fun AcceptedItem(
    group: GroupUiModel,
    handleEvent: (NotificationsEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(size_8_dp)
            )
            .padding(all = size_16_dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.join_request_approved, group.name, group.id),
            fontSize = font_size_14_sp,
            fontFamily = redHatDisplayFontFamily
        )
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
                        handleEvent(NotificationsEvent.SearchGroup)
                    },
                text = stringResource(id = R.string.search_new_group),
                fontSize = font_size_16_sp,
                fontFamily = redHatDisplayFontFamily,
                fontWeight = FontWeight.Bold,
                color =  MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = size_8_dp)
                    .clickable {
                        handleEvent(NotificationsEvent.GoToMain)
                    },
                text = stringResource(id = R.string.action_go_to_main),
                fontSize = font_size_16_sp,
                fontFamily = redHatDisplayFontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
    }
}