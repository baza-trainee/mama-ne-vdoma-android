package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_20_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp
import java.time.DayOfWeek

@Composable
fun ParentInfoDesk(
    modifier: Modifier = Modifier,
    name: String,
    address: String,
    avatar: Uri,
    showDeleteButton: Boolean = true,
    schedule: SnapshotStateMap<DayOfWeek, DayPeriod>,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(size_8_dp)
            )
            .padding(all = size_16_dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            AsyncImage(
                modifier = Modifier
                    .height(size_48_dp)
                    .width(size_48_dp)
                    .clip(CircleShape),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(avatar)
                    .placeholder(R.drawable.ic_user_no_photo)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_user_no_photo),
                fallback = painterResource(id = R.drawable.ic_user_no_photo),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = size_8_dp)
                    .fillMaxWidth()
                    .weight(1f),
                text = name,
                fontFamily = redHatDisplayFontFamily,
                fontSize = font_size_20_sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { onEdit() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            if (showDeleteButton)
                IconButton(onClick = { onDelete() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
        }

        if (address.isNotEmpty()) {
            Text(
                modifier = Modifier
                    .padding(top = size_8_dp)
                    .padding(horizontal = size_8_dp)
                    .fillMaxWidth(),
                text = stringResource(id = R.string.your_address),
                fontFamily = redHatDisplayFontFamily,
                fontSize = font_size_14_sp
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = size_8_dp)
                    .fillMaxWidth(),
                text = address,
                fontFamily = redHatDisplayFontFamily,
                fontSize = font_size_14_sp
            )
        }

        ScheduleInfoDesk(
            modifier = Modifier.padding(top = size_8_dp),
            schedule = schedule,
            dayText = stringResource(id = R.string.child_care_days),
            periodText = stringResource(id = R.string.child_care_hours)
        )
    }
}

@Composable
@Preview
fun ParenInfoDeskPreview() {
    ParentInfoDesk(
        name = "Somebody",
        address = "Somewhere",
        avatar = Uri.EMPTY,
        showDeleteButton = true,
        schedule = remember {
            mutableStateMapOf(
                DayOfWeek.MONDAY to DayPeriod(morning = true),
                DayOfWeek.TUESDAY to DayPeriod(wholeDay = true),
                DayOfWeek.WEDNESDAY to DayPeriod(noon = true),
                DayOfWeek.THURSDAY to DayPeriod(morning = true, afternoon = true),
                DayOfWeek.FRIDAY to DayPeriod(noon = true, afternoon = true),
                DayOfWeek.SATURDAY to DayPeriod(afternoon = true),
                DayOfWeek.SUNDAY to DayPeriod(wholeDay = true),
            )
        },
        onEdit = {},
        onDelete = {}
    )
}