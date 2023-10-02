package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.SemiTransparent

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SemiTransparent),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}