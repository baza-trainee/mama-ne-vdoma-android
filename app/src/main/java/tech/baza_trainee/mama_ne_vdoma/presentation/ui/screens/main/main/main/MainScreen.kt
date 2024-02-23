package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_16_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_20_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_124_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_128_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_24_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_2_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_4_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_54_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_64_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp

@Composable
fun MainScreen(
    screenState: MainViewState,
    handleEvent: (MainEvent) -> Unit
) {
    BackHandler { handleEvent(MainEvent.OnBack) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_16_dp)
                .height(size_128_dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size_8_dp)
                )
                .clickable {
                    handleEvent(MainEvent.Groups)
                }
                .padding(size_2_dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.my_groups),
                fontSize = font_size_20_sp,
                fontFamily = redHatDisplayFontFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Image(
                modifier = Modifier.height(size_124_dp),
                painter = painterResource(id = R.drawable.my_groups),
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_16_dp)
                .height(size_128_dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size_8_dp)
                )
                .clickable {
                    handleEvent(MainEvent.Search)
                }
                .padding(size_2_dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.title_search),
                fontSize = font_size_20_sp,
                fontFamily = redHatDisplayFontFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Image(
                modifier = Modifier.height(size_124_dp),
                painter = painterResource(id = R.drawable.search),
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_16_dp)
                .height(size_128_dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size_8_dp)
                )
                .clickable {
                    handleEvent(MainEvent.Account)
                }
                .padding(size_2_dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.title_account_info),
                fontSize = font_size_20_sp,
                fontFamily = redHatDisplayFontFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Image(
                modifier = Modifier.height(size_124_dp),
                painter = painterResource(id = R.drawable.my_account),
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = size_16_dp)
                .height(size_64_dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size_8_dp)
                )
                .clickable {
                    handleEvent(MainEvent.Notifications)
                }
                .padding(size_4_dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.title_my_notifications),
                fontSize = font_size_16_sp,
                fontFamily = redHatDisplayFontFamily,
                textAlign = TextAlign.Center
            )
            Image(
                modifier = Modifier.height(size_54_dp),
                painter = painterResource(id = R.drawable.notifications),
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = size_16_dp)
                .height(size_64_dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size_8_dp)
                )
                .clickable {
                    handleEvent(MainEvent.Settings)
                }
                .padding(size_4_dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.title_my_settings),
                fontSize = font_size_16_sp,
                fontFamily = redHatDisplayFontFamily,
                textAlign = TextAlign.Center
            )
            Image(
                modifier = Modifier.height(size_54_dp),
                painter = painterResource(id = R.drawable.account_settings),
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
        }
    }

    if (screenState.isLoading) LoadingIndicator()
}

@Composable
@Preview
fun MainScreenPreview() {
    MainScreen(
        screenState = MainViewState(),
        handleEvent = {}
    )
}