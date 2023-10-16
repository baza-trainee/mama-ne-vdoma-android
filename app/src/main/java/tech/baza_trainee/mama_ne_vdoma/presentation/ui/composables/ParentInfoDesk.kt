package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.domain.model.ScheduleModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import java.time.DayOfWeek

@Composable
@Preview
fun ParentInfoDesk(
    name: String = "Somebody",
    address: String = "Somewhere",
    avatar: Uri = Uri.EMPTY,
    schedule: ScheduleModel = ScheduleModel(
        mutableStateMapOf(
            DayOfWeek.MONDAY to DayPeriod(morning = true),
            DayOfWeek.TUESDAY to DayPeriod(wholeDay = true),
            DayOfWeek.WEDNESDAY to DayPeriod(noon = true),
            DayOfWeek.THURSDAY to DayPeriod(morning = true, afternoon = true),
            DayOfWeek.FRIDAY to DayPeriod(noon = true, afternoon = true),
            DayOfWeek.SATURDAY to DayPeriod(afternoon = true),
            DayOfWeek.SUNDAY to DayPeriod(wholeDay = true),
        )
    ),
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            AsyncImage(
                modifier = Modifier
                    .height(48.dp)
                    .width(48.dp)
                    .clip(CircleShape),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(avatar)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.no_photo),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .weight(1f),
                text = name,
                fontFamily = redHatDisplayFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { onEdit() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = { onDelete() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            text = "Ваша адреса",
            fontFamily = redHatDisplayFontFamily,
            fontSize = 14.sp
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            text = address,
            fontFamily = redHatDisplayFontFamily,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        val morning = schedule.schedule.filter { it.value.morning }.keys
        val noon = schedule.schedule.filter { it.value.noon }.keys
        val afternoon = schedule.schedule.filter { it.value.afternoon }.keys
        val wholeDay = schedule.schedule.filter { it.value.wholeDay }.keys

        val dayText = "Дні, коли ви можете доглядати за дітьми"
        val periodText = "Час, коли ви можете доглядати за дітьми"

        if (morning.isNotEmpty())
            DayScheduleRow(
                morning,
                Period.MORNING,
                dayText,
                periodText
            )
        if (noon.isNotEmpty())
            DayScheduleRow(
                noon,
                Period.NOON,
                dayText,
                periodText
            )
        if (afternoon.isNotEmpty())
            DayScheduleRow(
                afternoon,
                Period.AFTERNOON,
                dayText,
                periodText
            )
        if (wholeDay.isNotEmpty())
            DayScheduleRow(
                wholeDay,
                Period.WHOLE_DAY,
                dayText,
                periodText
            )
    }
}