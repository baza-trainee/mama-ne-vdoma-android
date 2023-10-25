package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.children_info

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.cards.ChildInfoDesk
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.headers.HeaderWithOptArrow
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun  ChildrenInfoScreen(
    modifier: Modifier = Modifier,
    screenState: State<ChildrenInfoViewState> = mutableStateOf(ChildrenInfoViewState()),
    onHandleChildrenInfoEvent: (ChildrenInfoEvent) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    SurfaceWithNavigationBars {
        Column(
            modifier = Modifier
                .imePadding()
                .fillMaxWidth()
        ) {
            HeaderWithOptArrow(
                modifier = Modifier.fillMaxWidth(),
                title = "Анкета дитини",
                onBack = onBack
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    items(screenState.value.children) { child ->
                        ChildInfoDesk(
                            modifier = Modifier.fillMaxWidth(),
                            child = child,
                            onEdit = {
                                onHandleChildrenInfoEvent(ChildrenInfoEvent.SetChild(it))
                                onEdit()
                            },
                            onDelete = {
                                onHandleChildrenInfoEvent(ChildrenInfoEvent.DeleteChild(it))
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onHandleChildrenInfoEvent(ChildrenInfoEvent.ResetChild)
                            onEdit()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Додати ще дитину",
                        fontFamily = redHatDisplayFontFamily,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .fillMaxWidth(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = onNext
            ) {
                ButtonText(
                    text = "Далі"
                )
            }
        }

        if (screenState.value.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun ChildrenInfoPreview() {
    ChildrenInfoScreen()
}
