package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R

@Composable
fun AddressNotCheckedDialog(
    title: String,
    onDismiss: () -> Unit = {}
) {

    GenericAlertDialog(
        icon = Icons.Filled.LocationOn,
        text = title,
        confirmButtonText = stringResource(id = R.string.got_it),
        confirmButtonAction = onDismiss,
        onDismissRequest = onDismiss
    )
}

@Preview
@Composable
private fun AddressNotCheckedDialogPreview() {
    AddressNotCheckedDialog(
        title = "Title",
        onDismiss = {}
    )
}
