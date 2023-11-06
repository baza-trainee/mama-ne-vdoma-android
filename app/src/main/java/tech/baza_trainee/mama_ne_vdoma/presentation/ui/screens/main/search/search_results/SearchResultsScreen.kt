package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_results

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.ParentCardInSearch
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState

@Composable
fun SearchResultsScreen(
    modifier: Modifier = Modifier,
    screenState: State<SearchResultsViewState> = mutableStateOf(SearchResultsViewState()),
    uiState: State<RequestState> = mutableStateOf(RequestState.Idle),
    handleEvent: (SearchResultsEvent) -> Unit = {}
) {
    BackHandler { handleEvent(SearchResultsEvent.OnBack) }

    val context = LocalContext.current

    when (val state = uiState.value) {
        RequestState.Idle -> Unit
        is RequestState.OnError -> {
            if (state.error.isNotBlank()) Toast.makeText(context, state.error, Toast.LENGTH_LONG)
                .show()
            handleEvent(SearchResultsEvent.ResetUiState)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            ParentCardInSearch(
                parent = screenState.value.parent
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(48.dp),
            onClick = {
                handleEvent(SearchResultsEvent.OnNewSearch)
            }
        ) {
            ButtonText(
                text = "Знайти ще користувачів"
            )
        }

        if (screenState.value.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun SearchResultsScreenPreview() {
    SearchResultsScreen()
}