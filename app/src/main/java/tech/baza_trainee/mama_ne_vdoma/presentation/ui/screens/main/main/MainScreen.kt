package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    screenState: State<MainViewState> = mutableStateOf(MainViewState()),
    handleEvent: (MainEvent) -> Unit = {}
) {
    BackHandler { handleEvent(MainEvent.OnBack) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    handleEvent(MainEvent.Groups)
                }
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Мої групи",
                fontSize = 20.sp,
                fontFamily = redHatDisplayFontFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Image(
                modifier = Modifier.height(124.dp),
                painter = painterResource(id = R.drawable.my_groups),
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    handleEvent(MainEvent.Search)
                }
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Пошук",
                fontSize = 20.sp,
                fontFamily = redHatDisplayFontFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Image(
                modifier = Modifier.height(124.dp),
                painter = painterResource(id = R.drawable.search),
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    handleEvent(MainEvent.Account)
                }
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Мій акаунт та інформація про дітей",
                fontSize = 20.sp,
                fontFamily = redHatDisplayFontFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Image(
                modifier = Modifier.height(124.dp),
                painter = painterResource(id = R.drawable.my_account),
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    handleEvent(MainEvent.Notifications)
                }
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Мої повідомлення",
                fontSize = 16.sp,
                fontFamily = redHatDisplayFontFamily,
                textAlign = TextAlign.Center
            )
            Image(
                modifier = Modifier.height(54.dp),
                painter = painterResource(id = R.drawable.notifications),
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    handleEvent(MainEvent.Settings)
                }
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Налаштування акаунту",
                fontSize = 16.sp,
                fontFamily = redHatDisplayFontFamily,
                textAlign = TextAlign.Center
            )
            Image(
                modifier = Modifier.height(54.dp),
                painter = painterResource(id = R.drawable.account_settings),
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
        }
    }

    if (screenState.value.isLoading) LoadingIndicator()
}

@Composable
@Preview
fun MainScreenPreview() {
    MainScreen()
}