package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.delay
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.Period
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.ScheduleScreenState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ChildScheduleGroup
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.rememberImeState
import java.time.DayOfWeek

@Composable
@Preview
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    screenState: State<ScheduleScreenState> = mutableStateOf(ScheduleScreenState()),
    onUpdateSchedule: (DayOfWeek, Period) -> Unit = { _, _ -> },
    onUpdateComment: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val imeState = rememberImeState()
        val scrollState = rememberScrollState()
        val density = LocalDensity.current.density
        val offset = (5000 * density).toInt()

        LaunchedEffect(key1 = imeState.value) {
            if (imeState.value) {
                delay(100)
                scrollState.scrollTo(offset)
            }
        }

        ConstraintLayout(
            modifier = modifier
                .verticalScroll(scrollState)
                .imePadding()
                .fillMaxWidth()
        ) {
            val (topBar, schedule, comment, btnNext) = createRefs()

            val topGuideline = createGuidelineFromTop(0.2f)

            Column(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                        bottom.linkTo(topGuideline)
                        height = Dimension.fillToConstraints
                    }
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                IconButton(
                    modifier = modifier
                        .padding(start = 16.dp, top = 16.dp)
                        .height(24.dp)
                        .width(24.dp),
                    onClick = { onBack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .padding(horizontal = 24.dp),
                    text = "Вкажіть, коли потрібно доглядати дитину",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            ChildScheduleGroup(
                modifier = modifier
                    .constrainAs(schedule) {
                        top.linkTo(topGuideline, 24.dp)
                    },
                scheduleModel = screenState.value.schedule,
                onValueChange = { day, period -> onUpdateSchedule(day, period) }
            )

            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .constrainAs(comment) {
                        top.linkTo(schedule.bottom, 16.dp)
                        bottom.linkTo(btnNext.top, 16.dp)
                    },
                value = screenState.value.comment,
                label = { Text("Нотатка") },
                onValueChange = { onUpdateComment(it) },
                minLines = 3,
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor =  MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor =  MaterialTheme.colorScheme.surface,
                    disabledContainerColor =  MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surface,
                )
            )

            Button(
                modifier = modifier
                    .constrainAs(btnNext) {
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(48.dp),
                onClick = onNext
            ) {
                Text(text = "Встановити розклад")
            }
        }
    }
}