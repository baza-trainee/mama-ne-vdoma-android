package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.PurpleGrey80
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_10_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_0_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_32_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_72_dp

@Composable
@Preview
fun MainNavigationBar(
    modifier: Modifier = Modifier,
    items: List<MainNavigationItem> = emptyList(),
    currentPage: Int = 0,
    onItemClicked: (Int) -> Unit = {}
) {
    NavigationBar(
        modifier = modifier
            .height(size_72_dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
            ),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = size_0_dp
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = size_16_dp)
                            .height(size_32_dp)
                            .background(
                                color = if (currentPage == index) PurpleGrey80
                                else MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(size_16_dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = null,
                            tint = if (currentPage == index) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }

                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = font_size_10_sp,
                        fontFamily = redHatDisplayFontFamily,
                        textAlign = TextAlign.Center,
                        color = if (currentPage == index) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )
                },
                selected = currentPage == index,
                alwaysShowLabel = true,
                onClick = { onItemClicked(index) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}

data class MainNavigationItem(
    val title: String,
    @DrawableRes val icon: Int
)