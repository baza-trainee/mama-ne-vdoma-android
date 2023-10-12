package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

@Composable
@Preview
fun HeaderWithToolbar(
    modifier: Modifier = Modifier,
    title: String = "Title",
    onBack: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary)
            .windowInsetsPadding(WindowInsets.statusBars)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        ToolbarWithAvatar(
            title = ""
        ) { onBack() }

        Spacer(modifier = modifier.height(16.dp))

        Text(
            modifier = modifier
                .padding(top = 16.dp)
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            text = title,
            fontSize = 24.sp,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = redHatDisplayFontFamily
        )

        Spacer(modifier = modifier.height(64.dp))
    }
}