package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tech.baza_trainee.mama_ne_vdoma.R

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    totalCount: Int,
    spaceBetween: Dp = 0.dp,
    onRate: (Int) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(totalCount) {
            val icon = if (it < rating)
                painterResource(id = R.drawable.ic_star)
            else
                painterResource(id = R.drawable.ic_star_empty)

            Image(
                modifier = Modifier.clickable { onRate(it + 1) },
                painter = icon,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(spaceBetween))
        }
    }
}

@Preview
@Composable
fun RatingBarPreview() {
    RatingBar(rating = 4, totalCount = 5) {}
}
