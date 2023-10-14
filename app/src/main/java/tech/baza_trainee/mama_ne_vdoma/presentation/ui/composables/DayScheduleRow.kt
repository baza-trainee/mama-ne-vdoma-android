package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.short
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayScheduleRow(
    daysOfWeek: Set<DayOfWeek>,
    period: Period,
    dayText: String,
    periodText: String
) {

    val localModifier = Modifier

    val chipColors = FilterChipDefaults.filterChipColors(
        containerColor = MaterialTheme.colorScheme.surface,
        selectedContainerColor = MaterialTheme.colorScheme.primary,
        disabledContainerColor = MaterialTheme.colorScheme.surface,
        disabledSelectedContainerColor = MaterialTheme.colorScheme.surface,
        labelColor = MaterialTheme.colorScheme.onBackground,
        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
        disabledLabelColor = MaterialTheme.colorScheme.onBackground,
        disabledLeadingIconColor = MaterialTheme.colorScheme.onBackground,
        selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
        disabledTrailingIconColor = MaterialTheme.colorScheme.onBackground,
        selectedTrailingIconColor = MaterialTheme.colorScheme.onPrimary
    )

    Text(
        text = dayText,
        fontFamily = redHatDisplayFontFamily,
        fontSize = 14.sp,
    )

    Row(
        modifier = localModifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChip(
            onClick = {},
            selected = daysOfWeek.contains(DayOfWeek.MONDAY),
            colors = chipColors,
            label = {
                Text(
                    text = DayOfWeek.MONDAY.short(),
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 14.sp,
                )
            }
        )
        FilterChip(
            onClick = {},
            selected = daysOfWeek.contains(DayOfWeek.TUESDAY),
            colors = chipColors,
            label = {
                Text(
                    text = DayOfWeek.TUESDAY.short(),
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 14.sp,
                )
            }
        )
        FilterChip(
            onClick = {},
            selected = daysOfWeek.contains(DayOfWeek.WEDNESDAY),
            colors = chipColors,
            label = {
                Text(
                    text = DayOfWeek.WEDNESDAY.short(),
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 14.sp,
                )
            }
        )
        FilterChip(
            onClick = {},
            selected = daysOfWeek.contains(DayOfWeek.THURSDAY),
            colors = chipColors,
            label = {
                Text(
                    text = DayOfWeek.THURSDAY.short(),
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 14.sp,
                )
            }
        )
        FilterChip(
            onClick = {},
            selected = daysOfWeek.contains(DayOfWeek.FRIDAY),
            colors = chipColors,
            label = {
                Text(
                    text = DayOfWeek.FRIDAY.short(),
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 14.sp,
                )
            }
        )
        FilterChip(
            onClick = {},
            selected = daysOfWeek.contains(DayOfWeek.SATURDAY),
            colors = chipColors,
            label = {
                Text(
                    text = DayOfWeek.SATURDAY.short(),
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 14.sp,
                )
            }
        )
        FilterChip(
            onClick = {},
            selected = daysOfWeek.contains(DayOfWeek.SUNDAY),
            colors = chipColors,
            label = {
                Text(
                    text = DayOfWeek.SUNDAY.short(),
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 14.sp,
                )
            }
        )
    }

    Spacer(modifier = localModifier.height(8.dp))

    Text(
        text = periodText,
        fontFamily = redHatDisplayFontFamily,
        fontSize = 14.sp,
    )

    Row(
        modifier = localModifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChip(
            onClick = {},
            selected = period == Period.MORNING || period == Period.WHOLE_DAY,
            colors = chipColors,
            label =  {
                Text(
                    text = "ранок",
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 14.sp,
                )
            }
        )
        FilterChip(
            onClick = {},
            selected = period == Period.NOON || period == Period.WHOLE_DAY,
            colors = chipColors,
            label =  {
                Text(
                    text = "обід",
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 14.sp,
                )
            }
        )
        FilterChip(
            onClick = {},
            selected = period == Period.AFTERNOON || period == Period.WHOLE_DAY,
            colors = chipColors,
            label =  {
                Text(
                    text = "вечір",
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 14.sp,
                )
            }
        )
    }
}