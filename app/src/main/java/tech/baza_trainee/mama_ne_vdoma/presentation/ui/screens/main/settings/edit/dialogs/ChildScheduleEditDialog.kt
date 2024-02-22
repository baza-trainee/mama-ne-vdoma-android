package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.DayPeriod
import tech.baza_trainee.mama_ne_vdoma.domain.model.updateSchedule
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ScheduleGroup
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.GrayText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Purple80
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ChildScheduleEditDialog(
    selectedChild: Int = 0,
    children: List<ChildEntity> = emptyList(),
    onSave: (Map<Int, SnapshotStateMap<DayOfWeek, DayPeriod>>, Map<Int, String>) -> Unit = {_,_ ->},
    onDismissRequest: () -> Unit = {}
) {
    var currentChild by rememberSaveable { mutableIntStateOf(selectedChild) }
    var schedules by rememberSaveable {
        val map = mutableMapOf<Int, SnapshotStateMap<DayOfWeek, DayPeriod>>().apply {
            children.forEachIndexed { index, childEntity ->
                put(index, childEntity.schedule)
            }
        }
        mutableStateOf(map.toMap())
    }
    var notes by rememberSaveable {
        val map = mutableMapOf<Int, String>().apply {
            children.forEachIndexed { index, childEntity ->
                put(index, childEntity.note)
            }
        }
        mutableStateOf(map.toMap())
    }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        ) {

            val (title, content, buttons) = createRefs()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        height = Dimension.wrapContent
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(0.8f),
                    text = stringResource(id = R.string.change_child_schedule),
                    fontSize = 16.sp,
                    fontFamily = redHatDisplayFontFamily
                )

                IconButton(
                    modifier = Modifier.weight(0.2f),
                    onClick = onDismissRequest
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "close"
                    )
                }
            }

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .constrainAs(content) {
                        top.linkTo(title.bottom, 8.dp)
                        bottom.linkTo(buttons.top, 8.dp)
                        height = Dimension.fillToConstraints
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (children.isNotEmpty()) {
                    when (children.size) {
                        1 -> Unit
                        2 -> {
                            Row(
                                modifier = Modifier
                                    .padding(all = 8.dp)
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .border(
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.onBackground
                                        ),
                                        shape = RoundedCornerShape(100.dp)
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxSize()
                                        .clip(
                                            shape = RoundedCornerShape(
                                                topStart = 100.dp,
                                                bottomStart = 100.dp
                                            )
                                        )
                                        .background(
                                            color = if (currentChild == 0) Purple80 else MaterialTheme.colorScheme.background,
                                            shape = RoundedCornerShape(
                                                topStart = 100.dp,
                                                bottomStart = 100.dp
                                            )
                                        )
                                        .align(Alignment.CenterVertically)
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) { currentChild = 0 },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = children[0].name,
                                        textAlign = TextAlign.Center
                                    )
                                }

                                VerticalDivider(
                                    thickness = 1.dp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxSize()
                                        .clip(
                                            shape = RoundedCornerShape(
                                                topEnd = 100.dp,
                                                bottomEnd = 100.dp
                                            )
                                        )
                                        .background(
                                            color = if (currentChild == 1) Purple80 else MaterialTheme.colorScheme.background,
                                            shape = RoundedCornerShape(
                                                topEnd = 100.dp,
                                                bottomEnd = 100.dp
                                            )
                                        )
                                        .align(Alignment.CenterVertically)
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) { currentChild = 1 },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = children[1].name,
                                        textAlign = TextAlign.Center,
                                        fontFamily = redHatDisplayFontFamily
                                    )
                                }
                            }
                        }

                        else -> {
                            var expanded by rememberSaveable { mutableStateOf(false) }
                            var searchRequest by rememberSaveable { mutableStateOf("") }
                            var searchedChildren by rememberSaveable { mutableStateOf(children) }

                            ExposedDropdownMenuBox(
                                modifier = Modifier.fillMaxWidth(),
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded },
                            ) {
                                OutlinedTextField(
                                    value = searchRequest,
                                    onValueChange = { value ->
                                        searchRequest = value
                                        searchedChildren =
                                            children.filter { it.name.contains(value) }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    label = { Text(stringResource(id = R.string.enter_name)) },
                                    placeholder = { Text(stringResource(id = R.string.name)) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                                    ),
                                    trailingIcon = {
                                        IconButton(
                                            onClick = {
                                                searchRequest = ""
                                                searchedChildren = children
                                            }
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_clear),
                                                contentDescription = "clear_search"
                                            )
                                        }
                                    },
                                    maxLines = 1
                                )

                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    scrollState = rememberScrollState()
                                ) {
                                    searchedChildren.forEach { child ->
                                        DropdownMenuItem(
                                            onClick = {
                                                searchRequest = child.name
                                                currentChild = children.indexOf(child)
                                                expanded = false
                                            },
                                            text = {
                                                Text(text = child.name)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                ScheduleGroup(
                    modifier = Modifier.fillMaxWidth(),
                    schedule = schedules[currentChild].orEmpty(),
                    onValueChange = { day, period ->
                        schedules = schedules.toMutableMap().apply {
                            put(
                                currentChild,
                                schedules[currentChild].orEmpty().updateSchedule(day, period)
                            )
                        }.toMap()
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextFieldWithError(
                    modifier = Modifier.fillMaxWidth(),
                    value = notes[currentChild].orEmpty(),
                    label = stringResource(id = R.string.note),
                    hint = stringResource(id = R.string.note_hint),
                    onValueChange = {
                        notes = notes.toMutableMap().apply {
                            put(currentChild, it)
                        }.toMap()
                    },
                    minLines = 3,
                    maxLines = 3,
                    isError = notes[currentChild].orEmpty().length > 1000
                )

                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.End),
                    text = stringResource(id = R.string.note_text_length),
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 12.sp,
                    color = GrayText
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(buttons) {
                        bottom.linkTo(parent.bottom)
                        height = Dimension.wrapContent
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    modifier = Modifier
                        .height(48.dp)
                        .padding(horizontal = 8.dp)
                        .weight(0.6f),
                    onClick = {
                        onSave(schedules, notes)
                        onDismissRequest()
                    }
                ) {
                    ButtonText(text = stringResource(id = R.string.action_save_changes))
                }
                OutlinedButton(
                    modifier = Modifier
                        .height(48.dp)
                        .padding(horizontal = 8.dp)
                        .weight(0.4f),
                    onClick = onDismissRequest,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    ButtonText(text = stringResource(id = R.string.action_close))
                }
            }
        }
    }
}