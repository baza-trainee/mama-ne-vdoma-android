package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.domain.model.ChildEntity
import tech.baza_trainee.mama_ne_vdoma.domain.model.Gender
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_14_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_21_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_82_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp

@Composable
@Preview
fun ChildCard(
    modifier: Modifier = Modifier,
    child: ChildEntity = ChildEntity(),
    infoOnly: Boolean = false,
    isSelected: Boolean = false,
    onSelected: (ChildEntity) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(size_82_dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(size_8_dp)
            )
            .clickable {
                onSelected(child)
            }
            .padding(size_8_dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = if (child.gender == Gender.BOY) R.drawable.ic_boy else R.drawable.ic_girl),
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .padding(horizontal = size_8_dp)
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = child.name,
                fontFamily = redHatDisplayFontFamily,
                fontSize = font_size_21_sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = stringResource(id = R.string.format_age, child.age),
                fontFamily = redHatDisplayFontFamily,
                fontSize = font_size_14_sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (!infoOnly) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onSelected(child) }
            )
        }
    }
}