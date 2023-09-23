package tech.baza_trainee.mama_ne_vdoma.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.ChildScheduleModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.Period
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Gray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Mama_ne_vdomaTheme
import java.time.format.TextStyle
import java.util.Locale

@Composable
@Preview
fun ChildScheduleGroup(
    modifier: Modifier = Modifier,
    schedule: State<ChildScheduleModel> = mutableStateOf(ChildScheduleModel())
) {
    Mama_ne_vdomaTheme {
        Column(
            modifier = modifier
                .padding(horizontal = 24.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(4.dp))
                .background(Gray),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = modifier
                        .wrapContentWidth()
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    text = Period.WHOLE_DAY.period,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = modifier
                        .wrapContentWidth()
                        .padding(horizontal = 8.dp),
                    text = Period.MORNING.period,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = modifier
                        .wrapContentWidth()
                        .padding(horizontal = 8.dp),
                    text = Period.NOON.period,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = modifier
                        .wrapContentWidth()
                        .padding(horizontal = 8.dp),
                    text = Period.AFTERNOON.period,
                    textAlign = TextAlign.Center
                )
            }
            val days = schedule.value.schedule
            days.keys.forEach {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .weight(1f),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val dayName = it.getDisplayName(TextStyle.FULL, Locale.getDefault())
                        .replaceFirstChar { it.uppercase() }

                    val wholeDayState = remember { mutableStateOf(days[it]?.wholeDay ?: false) }
                    val morningState = remember { mutableStateOf(days[it]?.morning ?: false) }
                    val noonState = remember { mutableStateOf(days[it]?.noon ?: false) }
                    val afternoonState = remember { mutableStateOf(days[it]?.afternoon ?: false) }

                    Box(
                        modifier = modifier
                            .border(
                                width = 2.dp,
                                color = if (wholeDayState.value) MaterialTheme.colorScheme.primary else Gray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .height(48.dp)
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                            .clickable {
                                wholeDayState.value = !wholeDayState.value
                                if (wholeDayState.value) {
                                    morningState.value = false
                                    noonState.value = false
                                    afternoonState.value = false
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dayName,
                            textAlign = TextAlign.Center
                        )
                    }
                    Box(
                        modifier = modifier
                            .height(48.dp)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Checkbox(
                            modifier = modifier
                                .border(
                                    width = 2.dp,
                                    color = if (morningState.value) MaterialTheme.colorScheme.primary else Gray,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            checked = morningState.value,
                            onCheckedChange = {
                                morningState.value = !morningState.value
                                if (morningState.value && noonState.value && afternoonState.value) {
                                    wholeDayState.value = true
                                    morningState.value = false
                                    noonState.value = false
                                    afternoonState.value = false
                                }
                            }
                        )
                    }

                    Box(
                        modifier = modifier
                            .height(48.dp)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Checkbox(
                            modifier = modifier
                                .border(
                                    width = 2.dp,
                                    color = if (noonState.value) MaterialTheme.colorScheme.primary else Gray,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            checked = noonState.value,
                            onCheckedChange = {
                                noonState.value = !noonState.value
                                if (morningState.value && noonState.value && afternoonState.value) {
                                    wholeDayState.value = true
                                    morningState.value = false
                                    noonState.value = false
                                    afternoonState.value = false
                                }
                            }
                        )
                    }

                    Box(
                        modifier = modifier
                            .height(48.dp)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Checkbox(
                            modifier = modifier
                                .border(
                                    width = 2.dp,
                                    color = if (afternoonState.value) MaterialTheme.colorScheme.primary else Gray,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            checked = afternoonState.value,
                            onCheckedChange = {
                                afternoonState.value = !afternoonState.value
                                if (morningState.value && noonState.value && afternoonState.value) {
                                    wholeDayState.value = true
                                    morningState.value = false
                                    noonState.value = false
                                    afternoonState.value = false
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}