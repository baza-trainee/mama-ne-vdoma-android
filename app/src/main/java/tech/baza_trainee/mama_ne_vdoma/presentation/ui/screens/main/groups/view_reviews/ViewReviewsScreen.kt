package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.view_reviews

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.RatingBar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.model.UserReviewUiModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Black
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.GrayText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_16_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_12_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_24_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_70_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState

@Composable
fun ViewReviewsScreen(
    screenState: ViewReviewsViewState,
    uiState: State<RequestState>,
    handleEvent: (ViewReviewsEvent) -> Unit
) {
    BackHandler { handleEvent(ViewReviewsEvent.OnBack) }

    val context = LocalContext.current

    when (val state = uiState.value) {
        is RequestState.OnError -> {
            if (state.error.isNotBlank()) Toast.makeText(
                context,
                state.error,
                Toast.LENGTH_LONG
            ).show()
            handleEvent(ViewReviewsEvent.ResetUiState)
        }

        else -> Unit
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = size_16_dp),
        verticalArrangement = Arrangement.spacedBy(size_8_dp)
    ) {
        items(screenState.reviews) {
            UserReviewCard(model = it)
        }
    }

    if (screenState.isLoading) LoadingIndicator()
}

@Composable
@Preview
fun UserReviewCard(
    modifier: Modifier = Modifier,
    model: UserReviewUiModel = UserReviewUiModel()
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(size_8_dp)
            )
            .padding(all = size_16_dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                modifier = Modifier
                    .height(size_70_dp)
                    .width(size_70_dp)
                    .clip(CircleShape),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(model.avatar)
                    .placeholder(R.drawable.ic_user_no_photo)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_user_no_photo),
                fallback = painterResource(id = R.drawable.ic_user_no_photo),
                contentDescription = "avatar",
                contentScale = ContentScale.FillBounds
            )
            RatingBar(
                rating = model.rating,
                totalCount = 5,
                onRate = {}
            )
        }

        Text(
            modifier = Modifier
                .wrapContentWidth()
                .padding(top = size_24_dp)
                .align(Alignment.Start),
            text = model.name,
            fontFamily = redHatDisplayFontFamily,
            fontSize = font_size_16_sp,
            color = Black,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier
                .wrapContentWidth()
                .padding(top = size_12_dp)
                .align(Alignment.Start),
            text = model.note,
            fontFamily = redHatDisplayFontFamily,
            fontSize = font_size_14_sp,
            color = Black
        )

        if (model.timestamp.isNotEmpty())
            Text(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = size_12_dp)
                    .align(Alignment.Start),
                text = model.timestamp,
                fontFamily = redHatDisplayFontFamily,
                fontSize = font_size_14_sp,
                color = GrayText
            )
    }
}

@Composable
@Preview
fun ViewReviewsScreenPreview() {
    ViewReviewsScreen(
        screenState = ViewReviewsViewState(),
        uiState = remember { mutableStateOf(RequestState.Idle) },
        handleEvent = {}
    )
}