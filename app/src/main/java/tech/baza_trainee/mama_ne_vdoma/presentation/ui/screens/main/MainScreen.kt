package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.GroupInfoDesk
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.MainNavigationBar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.ToolbarWithAvatar

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    SurfaceWithNavigationBars {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = { ToolbarWithAvatar() },
            bottomBar = { MainNavigationBar() }
        ) {
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .consumeWindowInsets(it)
            ) {
                GroupInfoDesk()
            }
        }
    }
}

@Composable
@Preview
fun MainScreenPreview() {
    MainScreen()
}