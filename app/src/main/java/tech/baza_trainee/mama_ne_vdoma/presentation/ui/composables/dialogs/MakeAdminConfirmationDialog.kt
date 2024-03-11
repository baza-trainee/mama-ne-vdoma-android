package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R

@Composable
fun MakeAdminConfirmationDialog(
    memberName: String,
    onDismiss: () -> Unit,
    onMakeAdmin: () -> Unit
) {
    GenericAlertDialog(
        icon = Icons.Filled.Error,
        text = stringResource(id = R.string.make_admin_info, memberName),
        confirmButtonText = stringResource(id = R.string.action_yes_i_do),
        confirmButtonAction = onMakeAdmin,
        dismissButtonText = stringResource(id = R.string.action_refuse),
        onDismissRequest = onDismiss
    )
}

@Preview
@Composable
private fun MakeAdminConfirmationDialogPreview() {
    MakeAdminConfirmationDialog(
        memberName = "Username",
        onDismiss = {},
        onMakeAdmin = {}
    )
}
