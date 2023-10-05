package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopBarWithoutArrow(
    modifier: Modifier = Modifier,
    title: String = "Title",
    info: String = ""
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary)
            .windowInsetsPadding(WindowInsets.statusBars)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = modifier
                .padding(top = 16.dp)
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            text = title,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = redHatDisplayFontFamily
        )

        if (info.isNotEmpty()) {
            var isOverflowed by remember { mutableStateOf(false) }

            if (isOverflowed)
                Text(
                    modifier = modifier
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                        .basicMarquee(),
                    text = info,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = redHatDisplayFontFamily
                )
            else
                Text(
                    modifier = modifier
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(),
                    text = info,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = redHatDisplayFontFamily,
                    maxLines = 3,
                    minLines = 3,
                    onTextLayout = {
                        isOverflowed = it.hasVisualOverflow
                    }
                )
        }
    }
}