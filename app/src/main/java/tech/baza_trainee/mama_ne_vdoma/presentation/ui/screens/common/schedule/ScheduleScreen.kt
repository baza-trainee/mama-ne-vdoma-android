package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.schedule

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.baza_trainee.mama_ne_vdoma.domain.model.Period
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ScaffoldWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ScheduleScreenGroup
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithOptArrow
import java.time.DayOfWeek

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    title: String,
    screenState: ScheduleViewState,
    onUpdateSchedule: (DayOfWeek, Period) -> Unit,
    onUpdateComment: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    ScaffoldWithNavigationBars(
        topBar = {
            HeaderWithOptArrow(
                modifier = Modifier.fillMaxWidth(),
                title = title,
                onBack = onBack
            )
        }
    ) { paddingValues ->
        BackHandler { onBack() }

        ScheduleScreenGroup(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .imePadding(),
            screenState = screenState,
            onUpdateSchedule = onUpdateSchedule,
            onUpdateComment = onUpdateComment,
            onNext = onNext
        )
    }
}

@Composable
@Preview
fun ScheduleScreenPreview() {
    ScheduleScreen(
        modifier = Modifier,
        title = "Title",
        screenState = ScheduleViewState(),
        onUpdateSchedule = { _, _ -> },
        onUpdateComment = {},
        onNext = {},
        onBack = {}
    )
}