package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_20_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp
import java.time.DayOfWeek
import java.util.UUID

@Composable
fun ChildInfoDesk(
    modifier: Modifier = Modifier,
    child: ChildEntity,
    canEdit: Boolean = true,
    onEdit: (String) -> Unit = {},
    canDelete: Boolean = true,
    onDelete: (String) -> Unit = {}
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
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Image(
                painter = painterResource(id = if (child.gender == Gender.BOY) R.drawable.ic_boy else R.drawable.ic_girl),
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = size_8_dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = child.name,
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = font_size_20_sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(id = R.string.format_age, child.age),
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = font_size_14_sp
                )
            }
            if (canEdit)
                IconButton(onClick = { onEdit(child.childId) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            if (canDelete)
                IconButton(onClick = { onDelete(child.childId) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
        }

        Spacer(modifier = Modifier.height(size_8_dp))

        ScheduleInfoDesk(
            schedule = child.schedule,
            dayText = stringResource(id = R.string.child_care_days),
            periodText = stringResource(id = R.string.child_care_hours)
        )
    }
}

@Composable
@Preview
fun ChildInfoDeskPreview() {
    ChildInfoDesk(
        child = ChildEntity(
            childId = UUID.randomUUID().toString(),
            name = "Іванко",
            age = "5",
            gender = Gender.BOY,
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
            }
        ),
        canEdit = true,
        onEdit = {},
        canDelete = true,
        onDelete = {}
    )
}