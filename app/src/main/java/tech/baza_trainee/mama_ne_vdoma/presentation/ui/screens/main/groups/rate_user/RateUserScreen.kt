package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.rate_user

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.Rating
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.RatingBar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.SuccessDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.GrayText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_12_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_20_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_120_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_2_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_32_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.showToast

@Composable
fun RateUserScreen(
    screenState: RateUserViewState,
    uiState: RateUserUiState,
    handleEvent: (RateUserEvent) -> Unit
) {
    BackHandler { handleEvent(RateUserEvent.OnBack) }

    val context = LocalContext.current

    var showRatingSetDialog by remember { mutableStateOf(false) }

    when (uiState) {
        is RateUserUiState.OnError -> {
            context.showToast(uiState.error)
            handleEvent(RateUserEvent.ResetUiState)
        }

        RateUserUiState.OnSet -> showRatingSetDialog = true

        else -> Unit
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(screenState.avatar)
                .placeholder(R.drawable.ic_user_no_photo)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.ic_user_no_photo),
            fallback = painterResource(id = R.drawable.ic_user_no_photo),
            contentDescription = "avatar",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .padding(top = size_16_dp)
                .width(size_120_dp)
                .height(size_120_dp)
                .clip(CircleShape)
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_16_dp),
            text = screenState.name,
            fontSize = font_size_20_sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = redHatDisplayFontFamily
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_16_dp, end = size_8_dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Rating(
                rating = screenState.rating,
                backgroundColor = Color.Transparent
            )

            Row(
                modifier = Modifier
                    .padding(start = size_8_dp)
                    .clickable {
                        handleEvent(RateUserEvent.ViewReviews)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.padding(end = size_2_dp),
                    painter = painterResource(id = R.drawable.ic_reviews),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(start = size_2_dp),
                    text = screenState.reviews.toString(),
                    fontSize = font_size_14_sp,
                    fontFamily = redHatDisplayFontFamily
                )
            }
        }

        RatingBar(
            modifier = Modifier.padding(top = size_32_dp),
            rating = screenState.currentRating,
            totalCount = 5,
            spaceBetween = size_8_dp,
            onRate = { handleEvent(RateUserEvent.SetRating(it)) }
        )

        OutlinedTextFieldWithError(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_32_dp),
            value = screenState.note,
            label = stringResource(id = R.string.user_review_info),
            hint = stringResource(id = R.string.user_review_hint),
            onValueChange = { handleEvent(RateUserEvent.SetNote(it)) },
            maxLines = 3,
            isError = screenState.note.length > 500
        )

        Text(
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.Start)
                .padding(horizontal = size_16_dp),
            text = stringResource(id = R.string.user_review_length),
            fontFamily = redHatDisplayFontFamily,
            fontSize = font_size_12_sp,
            color = GrayText
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = size_16_dp)
                .height(size_48_dp),
            onClick = {
                handleEvent(RateUserEvent.OnSave)
            }
        ) {
            ButtonText(
                text = stringResource(id = R.string.action_rate_user)
            )
        }
    }

    if (showRatingSetDialog) {
        SuccessDialog(
            info = stringResource(id = R.string.user_rated_info),
            onDismiss = { showRatingSetDialog = false },
            onClick = {
                showRatingSetDialog = false
                handleEvent(RateUserEvent.GoToMain)
            }
        )
    }
}

@Composable
@Preview
fun RateUserScreenPreview() {
    RateUserScreen(
        screenState = RateUserViewState(),
        uiState = RateUserUiState.Idle,
        handleEvent = {}
    )
}