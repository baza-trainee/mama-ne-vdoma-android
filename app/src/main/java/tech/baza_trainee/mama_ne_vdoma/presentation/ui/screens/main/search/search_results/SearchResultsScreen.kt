package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.search.search_results

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.ParentCardInSearch
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun SearchResultsScreen(
    screenState: SearchResultsViewState,
    uiState: RequestState,
    handleEvent: (SearchResultsEvent) -> Unit
) {
    BackHandler { handleEvent(SearchResultsEvent.OnBack) }

    val context = LocalContext.current

    when (uiState) {
        RequestState.Idle -> Unit
        is RequestState.OnError -> {
            context.showToast(uiState.error)
            handleEvent(SearchResultsEvent.ResetUiState)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        ParentCardInSearch(
            modifier = Modifier.padding(top = size_16_dp),
            parent = screenState.parent
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = size_16_dp)
                .height(size_48_dp),
            onClick = {
                handleEvent(SearchResultsEvent.OnNewSearch)
            }
        ) {
            ButtonText(
                text = stringResource(id = R.string.action_find_more_users)
            )
        }

        if (screenState.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun SearchResultsScreenPreview() {
    SearchResultsScreen(
        screenState = SearchResultsViewState(),
        uiState = RequestState.Idle,
        handleEvent = {}
    )
}