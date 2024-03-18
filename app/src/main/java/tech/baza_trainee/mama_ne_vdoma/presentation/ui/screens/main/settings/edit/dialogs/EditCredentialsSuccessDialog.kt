package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.GenericAlertDialog

@Preview
@Composable
fun EditCredentialsSuccessDialog(
    title: String = "Title",
    onDismissRequest: () -> Unit = {},
) {
    GenericAlertDialog(
        text = title,
        confirmButtonText = stringResource(id = R.string.action_go_to_login),
        confirmButtonAction = onDismissRequest,
        onDismissRequest = {}
    )
}