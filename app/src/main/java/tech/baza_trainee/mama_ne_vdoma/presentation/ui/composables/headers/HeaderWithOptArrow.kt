package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_18_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_128_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_160_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_24_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun HeaderWithOptArrow(
    modifier: Modifier = Modifier,
    title: String = "Title",
    info: String = "",
    onBack: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary)
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(if (info.isNotEmpty()) size_160_dp else size_128_dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (onBack != null) Arrangement.Top else Arrangement.SpaceBetween
    ) {
        if (onBack != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier
                        .padding(start = size_16_dp, top = size_16_dp)
                        .height(size_24_dp)
                        .width(size_24_dp),
                    onClick = { onBack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = size_16_dp),
            text = title,
            fontSize = 24.sp,
            textAlign = if (onBack != null) TextAlign.Start else TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = redHatDisplayFontFamily
        )

        if (info.isNotEmpty()) {
            var isOverflowed by remember { mutableStateOf(false) }

            if (isOverflowed)
                Text(
                    modifier = Modifier
                        .padding(horizontal = size_16_dp, vertical = size_8_dp)
                        .fillMaxWidth()
                        .basicMarquee(),
                    text = info,
                    fontSize = font_size_14_sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = redHatDisplayFontFamily
                )
            else
                Text(
                    modifier = Modifier
                        .padding(horizontal = size_16_dp, vertical = size_8_dp)
                        .fillMaxWidth(),
                    text = info,
                    fontSize = font_size_14_sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = redHatDisplayFontFamily,
                    maxLines = 3,
                    minLines = 3,
                    onTextLayout = {
                        isOverflowed = it.hasVisualOverflow
                    },
                    lineHeight = font_size_18_sp
                )
        }
    }
}