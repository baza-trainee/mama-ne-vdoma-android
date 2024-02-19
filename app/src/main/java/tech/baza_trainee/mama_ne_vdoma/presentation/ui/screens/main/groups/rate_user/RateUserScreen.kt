package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.rate_user

import android.widget.Toast
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
import androidx.compose.runtime.State
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.ButtonText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.Rating
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.RatingBar
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.dialogs.SuccessDialog
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.GrayText
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

@Composable
fun RateUserScreen(
    screenState: RateUserViewState,
    uiState: State<RateUserUiState>,
    handleEvent: (RateUserEvent) -> Unit
) {
    BackHandler { handleEvent(RateUserEvent.OnBack) }

    val context = LocalContext.current

    var showRatingSetDialog by remember { mutableStateOf(false) }

    when (val state = uiState.value) {
        is RateUserUiState.OnError -> {
            if (state.error.isNotBlank()) Toast.makeText(
                context,
                state.error,
                Toast.LENGTH_LONG
            ).show()
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
                .padding(top = 16.dp)
                .width(120.dp)
                .height(120.dp)
                .clip(CircleShape)
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            text = screenState.name,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = redHatDisplayFontFamily
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Rating(
                rating = screenState.rating,
                backgroundColor = Color.Transparent
            )

            Row(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable {
                        handleEvent(RateUserEvent.ViewReviews)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.padding(end = 2.dp),
                    painter = painterResource(id = R.drawable.ic_reviews),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(start = 2.dp),
                    text = screenState.reviews.toString(),
                    fontSize = 14.sp,
                    fontFamily = redHatDisplayFontFamily
                )
            }
        }

        RatingBar(
            modifier = Modifier.padding(top = 32.dp),
            rating = screenState.currentRating,
            totalCount = 5,
            spaceBetween = 8.dp,
            onRate = { handleEvent(RateUserEvent.SetRating(it)) }
        )

        OutlinedTextFieldWithError(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            value = screenState.note,
            label = "Залишити відгук",
            hint = "Залишити відгук...",
            onValueChange = { handleEvent(RateUserEvent.SetNote(it)) },
            maxLines = 3,
            isError = screenState.note.length > 500
        )

        Text(
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.Start)
                .padding(horizontal = 16.dp),
            text = "Опис може містити кирилицю або латиницю до 500 символів",
            fontFamily = redHatDisplayFontFamily,
            fontSize = 12.sp,
            color = GrayText
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(48.dp),
            onClick = {
                handleEvent(RateUserEvent.OnSave)
            }
        ) {
            ButtonText(
                text = "Подати відгук"
            )
        }
    }

    if (showRatingSetDialog) {
        SuccessDialog(
            info = "Дякуємо за ваш відгук",
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
        uiState = remember { mutableStateOf(RateUserUiState.Idle) },
        handleEvent = {}
    )
}