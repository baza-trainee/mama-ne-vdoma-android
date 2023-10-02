package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.ScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm.UserSettingsViewModel

@Composable
fun ParentScheduleScreen(
    viewModel: UserSettingsViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    ScheduleScreen(
        title = "Визначіть свій графік, коли можете доглядати дітей",
        screenState = viewModel.parentScheduleViewState.collectAsStateWithLifecycle(),
        onUpdateSchedule = { day, period ->  },
        onNext = onNext,
        onBack = onBack
    )
}