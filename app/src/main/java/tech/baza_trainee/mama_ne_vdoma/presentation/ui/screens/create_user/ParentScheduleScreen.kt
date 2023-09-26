package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

import androidx.compose.runtime.Composable
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm.UserSettingsViewModel

@Composable
fun ParentScheduleFunc(
    viewModel: UserSettingsViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    ScheduleScreen(
        onNext = onNext,
        onBack = onBack
    )
}