package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.ScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm.UserSettingsViewModel

@Composable
fun ChildScheduleScreen(
    viewModel: UserSettingsViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    viewModel.setCurrentChildSchedule()

    ScheduleScreen(
        title = "Вкажіть, коли потрібно доглядати дитину",
        screenState = viewModel.childScheduleScreenState.collectAsStateWithLifecycle(),
        comment = viewModel.childComment,
        onUpdateSchedule = { day, period -> viewModel.updateChildSchedule(day, period) },
        onUpdateComment = { viewModel.updateChildComment(it) },
        onNext = onNext,
        onBack = onBack
    )
}
