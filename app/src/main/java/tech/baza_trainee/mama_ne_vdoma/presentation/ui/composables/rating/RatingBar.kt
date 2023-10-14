package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.rating

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.LightGrayStarFeedback
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.YellowStarFeedback

@Composable
fun RatingBar(
    currentRating: Int,
    onRatingChanged: (newRating: Int) -> Unit
) {
    Row(
        modifier = Modifier.selectableGroup(),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            RatingStar(
                isFilled = i <= currentRating,
                onClick = { onRatingChanged(i) }
            )
            if (i < 5) Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
fun RatingStar(
    isFilled: Boolean,
    onClick: () -> Unit
) {
    val starImage = if (isFilled) Icons.Filled.Star else Icons.Filled.Star

    Icon(
        imageVector = starImage,
        contentDescription = null,
        tint = if (isFilled) YellowStarFeedback else LightGrayStarFeedback,
        modifier = Modifier
            .size(24.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    )
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    RatingBar(currentRating = 3, onRatingChanged = {})
}