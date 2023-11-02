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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ScheduleGroup
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.GrayText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Purple80
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ChildScheduleEditDialog(
    modifier: Modifier = Modifier,
    selectedChild: Int = 0,
    children: List<ChildEntity> = emptyList(),
    onEditSchedule: (Int, DayOfWeek, Period) -> Unit = { _, _, _ -> },
    onEditNote: (Int, String) -> Unit = {_,_ -> },
    onSave: () -> Unit = {},
    onRestore: (Int) -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    var currentChild by rememberSaveable { mutableIntStateOf(selectedChild) }

    AlertDialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(0.8f),
                    text = "Внесіть зміни в розклад, коли потрібно доглядати дитину",
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

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
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
                                        ) {
                                            currentChild = 0
                                        },
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
                                        ) {
                                            currentChild = 1
                                        },
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
                                    label = { Text("Введіть ім’я") },
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
                    scheduleModel = children[currentChild].schedule,
                    onValueChange = { day, period -> onEditSchedule(currentChild, day, period) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = children[currentChild].note,
                    label = { Text("Нотатка") },
                    onValueChange = { onEditNote(currentChild, it) },
                    minLines = 2,
                    maxLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                    ),
                    textStyle = TextStyle(
                        fontFamily = redHatDisplayFontFamily
                    )
                )

                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.End),
                    text = "до 1000 символів",
                    fontFamily = redHatDisplayFontFamily,
                    fontSize = 12.sp,
                    color = GrayText
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    modifier = Modifier
                        .height(48.dp)
                        .padding(horizontal = 8.dp)
                        .weight(0.6f),
                    onClick = {
                        onSave()
                        onDismissRequest()
                    }
                ) {
                    ButtonText(text = "Зберегти зміни")
                }
                OutlinedButton(
                    modifier = Modifier
                        .height(48.dp)
                        .padding(horizontal = 8.dp)
                        .weight(0.4f),
                    onClick = {
                        onRestore(currentChild)
                        onDismissRequest()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    ButtonText(text = "Закрити")
                }
            }
        }
    }
}