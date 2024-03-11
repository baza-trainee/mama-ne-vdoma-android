package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.group_details.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.GenericAlertDialog

@Composable
fun KickUserDialog(
    userName: String,
    onDismiss: () -> Unit,
    onKick: () -> Unit
) {
    GenericAlertDialog(
        icon = Icons.Filled.Error,
        text = stringResource(id = R.string.sure_you_want_kick, userName),
        confirmButtonText = stringResource(id = R.string.action_yes_kick),
        confirmButtonAction = onKick,
        dismissButtonText = stringResource(id = R.string.no),
        onDismissRequest = onDismiss
    )
}

@Preview
@Composable
private fun KickUserDialogPreview() {
    KickUserDialog(
        userName = "UserName",
        onDismiss = {},
        onKick = {}
    )
}
