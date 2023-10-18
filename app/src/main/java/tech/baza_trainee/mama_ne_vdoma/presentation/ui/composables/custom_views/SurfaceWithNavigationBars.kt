package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SurfaceWithNavigationBars(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        content()
    }
}