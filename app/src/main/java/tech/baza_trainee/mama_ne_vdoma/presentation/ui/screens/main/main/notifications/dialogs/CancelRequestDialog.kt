package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.GenericAlertDialog

@Composable
fun CancelRequestDialog(
    groupName: String,
    onDismiss: () -> Unit,
    onCancel: () -> Unit
) {
    GenericAlertDialog(
        icon = Icons.Filled.Error,
        text = stringResource(id = R.string.cancel_join_request_info, groupName),
        confirmButtonText = stringResource(id = R.string.action_yes_cancel),
        confirmButtonAction = onCancel,
        dismissButtonText = stringResource(id = R.string.no),
        onDismissRequest = onDismiss
    )
}

@Preview
@Composable
private fun CancelRequestDialogPreview() {
    CancelRequestDialog(
        groupName = "Attention",
        onDismiss = {},
        onCancel = {}
    )
}
