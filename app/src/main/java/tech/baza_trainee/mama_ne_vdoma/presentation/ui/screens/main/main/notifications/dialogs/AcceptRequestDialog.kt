package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.GenericAlertDialog

@Composable
fun AcceptRequestDialog(
    groupName: String,
    onDismiss: () -> Unit
) {
    GenericAlertDialog(
        icon = Icons.Filled.Error,
        text = stringResource(id = R.string.join_request_accepted, groupName),
        confirmButtonText = stringResource(id = R.string.action_close),
        confirmButtonAction = onDismiss,
        onDismissRequest = onDismiss
    )
}

@Preview
@Composable
private fun AcceptRequestDialogPreview() {
    AcceptRequestDialog(
        groupName = "groupName",
        onDismiss = {}
    )
}
