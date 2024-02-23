package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.GroupUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_18_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp

@Composable
fun MakeAdminDialog(
    group: GroupUiModel,
    onDismiss: () -> Unit,
    onMakeAdmin: (String, String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(size_8_dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = size_16_dp, vertical = size_8_dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var selectedId by remember { mutableStateOf("") }
            var selectedName by remember { mutableStateOf("") }

            Text(
                text = stringResource(id = R.string.make_admin),
                fontSize = font_size_18_sp,
                fontFamily = redHatDisplayFontFamily,
                fontWeight = FontWeight.Bold
            )

            LazyColumn(
                modifier = Modifier.padding(size_8_dp)
            ) {
                items(group.members.sortedBy { it.name }) { member ->
                    if (member.id != group.adminId) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = member.name,
                                fontSize = font_size_14_sp,
                                fontFamily = redHatDisplayFontFamily
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Checkbox(
                                checked = selectedId == member.id,
                                onCheckedChange = {
                                    if (it) {
                                        selectedId = member.id
                                        selectedName = member.name
                                    } else {
                                        selectedId = ""
                                        selectedName = ""
                                    }
                                }
                            )
                        }
                    }
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(size_16_dp)
                    .height(size_48_dp),
                onClick = { onMakeAdmin(selectedId, selectedName) },
                enabled = selectedId != ""
            ) {
                ButtonText(
                    text = stringResource(id = R.string.action_make_admin)
                )
            }
        }
    }
}