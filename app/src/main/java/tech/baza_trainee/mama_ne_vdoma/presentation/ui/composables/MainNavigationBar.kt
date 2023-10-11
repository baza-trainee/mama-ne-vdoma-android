package tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Purple40
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.PurpleGrey80
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

@Composable
@Preview
fun MainNavigationBar(
    modifier: Modifier = Modifier
) {
    BottomNavigation(
        modifier = Modifier.height(72.dp),
        backgroundColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        elevation = 2.dp
    ) {
        var selectedIndex by remember { mutableIntStateOf(0) }
        val tabContents = listOf(
            "Головна" to R.drawable.ic_home,
            "Групи" to R.drawable.ic_group,
            "Пошук" to R.drawable.ic_search,
            "Налаштування" to R.drawable.ic_settings
        )
        tabContents.forEachIndexed { index, pair: Pair<String, Int> ->
            BottomNavigationItem(
                icon = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(32.dp)
                            .background(
                                color = if (selectedIndex == index) PurpleGrey80
                                else MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = pair.second),
                            contentDescription = null,
                            tint = if (selectedIndex == index) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }

                },
                label = {
                    Text(
                        text = pair.first,
                        fontSize = 10.sp,
                        fontFamily = redHatDisplayFontFamily,
                        textAlign = TextAlign.Center,
                        color = if (selectedIndex == index) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )
                },
                selected = selectedIndex == index,
                alwaysShowLabel = true,
                onClick = {
                    selectedIndex = index
                },
                selectedContentColor = Purple40,
                unselectedContentColor = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}