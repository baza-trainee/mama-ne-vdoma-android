package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_28_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_2_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_4_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_64_dp

@Composable
@Preview
fun Rating(
    modifier: Modifier = Modifier,
    rating: Float = 5.0f,
    backgroundColor: Color = Color.White,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .height(size_28_dp)
            .width(size_64_dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(size_4_dp)
            )
            .clickable { onClick() }
            .padding(horizontal = size_4_dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.padding(end = size_2_dp),
            painter = painterResource(id = R.drawable.ic_star),
            contentDescription = "rating"
        )
        Text(
            modifier = Modifier.padding(start = size_2_dp),
            text = String.format("%.1f", rating),
            fontSize = font_size_14_sp,
            fontFamily = redHatDisplayFontFamily
        )
    }
}
