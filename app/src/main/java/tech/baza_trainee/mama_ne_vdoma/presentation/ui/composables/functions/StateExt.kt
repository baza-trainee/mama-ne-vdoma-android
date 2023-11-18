package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow

@Composable
fun <T> StateFlow<T>.asStateWithLifecycle(): T {
    val state by collectAsStateWithLifecycle()
    return state
}