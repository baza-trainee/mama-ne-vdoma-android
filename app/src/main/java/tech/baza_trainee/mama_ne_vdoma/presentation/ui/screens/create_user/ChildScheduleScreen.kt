package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm.UserSettingsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.composables.ScheduleScreen

@Composable
fun ChildScheduleFunc(
    viewModel: UserSettingsViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    viewModel.setCurrentChildSchedule()
    ScheduleScreen(
        title = "Вкажіть, коли потрібно доглядати дитину",
        screenState = viewModel.childScheduleScreenState.collectAsStateWithLifecycle(),
        onUpdateSchedule = { day, period -> viewModel.updateChildSchedule(day, period) },
        onNext = onNext,
        onBack = onBack
    )
}
