package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs


import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R

@Composable
fun SuccessDialog(
    info: String,
    onDismiss: () -> Unit,
    onClick: () -> Unit
) {
    GenericAlertDialog(
        icon = painterResource(id = R.drawable.ic_ok),
        text = info,
        confirmButtonText = stringResource(id = R.string.action_go_to_main),
        confirmButtonAction = onClick,
        onDismissRequest = onDismiss
    )
}

@Preview
@Composable
private fun SuccessDialogPreview() {
    SuccessDialog(
        info = "Attention",
        onDismiss = {},
        onClick = {}
    )
}
