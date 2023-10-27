package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCredentialsSuccessDialog(
    modifier: Modifier = Modifier,
    title: String = "Title",
    onDismissRequest: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = {}
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                text = title,
                fontSize = 14.sp,
                fontFamily = redHatDisplayFontFamily,
                lineHeight = 18.sp
            )

            Text(
                text = "На головну",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.End)
                    .clickable { onDismissRequest() }
                    .padding(16.dp)
            )
        }
    }
}