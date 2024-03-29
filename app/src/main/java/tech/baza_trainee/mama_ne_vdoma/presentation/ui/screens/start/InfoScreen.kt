package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.start

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.launch
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.Indicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithNavigationBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.font_size_16_sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_16_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_24_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_48_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_4_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_70_dp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.size_8_dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfoScreen(onCreate: () -> Unit) {
    val scrollCoroutineScope = rememberCoroutineScope()

    val pageTextContent = listOf(
        stringResource(id = R.string.info_screen_hint_1),
        stringResource(id = R.string.info_screen_hint_2),
        stringResource(id = R.string.info_screen_hint_3)
    )

    val pagerImageContent = listOf(
        R.drawable.info_1,
        R.drawable.info_2,
        R.drawable.info_3
    )

    val pagerState = rememberPagerState(pageCount = { pageTextContent.size })

    SurfaceWithNavigationBars {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (pager, footer) = createRefs()

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .constrainAs(pager) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(footer.top, size_16_dp)
                    }
                    .fillMaxSize()
            ) { page ->
                ConstraintLayout(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val (image, title, info) = createRefs()

                    Image(
                        modifier = Modifier
                            .constrainAs(image) {
                                top.linkTo(parent.top)
                            }
                            .fillMaxWidth()
                            .fillMaxHeight(0.7f),
                        alignment = Alignment.TopCenter,
                        painter = painterResource(id = pagerImageContent[page]),
                        contentDescription = pageTextContent[page],
                        contentScale = ContentScale.FillHeight
                    )
                    Text(
                        text = stringResource(id = R.string.title_info_screen),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = size_24_dp)
                            .constrainAs(title) {
                                top.linkTo(image.bottom, size_8_dp)
                                height = Dimension.wrapContent
                            },
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontFamily = redHatDisplayFontFamily
                    )
                    Text(
                        text = pageTextContent[page],
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = size_24_dp)
                            .constrainAs(info) {
                                top.linkTo(title.bottom, size_16_dp)
                                height = Dimension.wrapContent
                            },
                        fontSize = font_size_16_sp,
                        textAlign = TextAlign.Center,
                        fontFamily = redHatDisplayFontFamily
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(footer) {
                        bottom.linkTo(parent.bottom, size_16_dp)
                        height = Dimension.preferredValue(size_70_dp)
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val isLastPage by remember {
                    derivedStateOf { pagerState.currentPage == 2 }
                }

                AnimatedVisibility(
                    visible = !isLastPage,
                    enter = slideInHorizontally(initialOffsetX = { -it }),
                    exit = slideOutHorizontally(targetOffsetX = { -it })
                ) {
                    ConstraintLayout(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val (indicator, btnSkip, btnNext) = createRefs()

                        Row(
                            modifier = Modifier
                                .constrainAs(indicator) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                }
                                .padding(horizontal = size_16_dp)
                        ) {
                            repeat(pageTextContent.size) { iteration ->
                                val isSelected by remember {
                                    derivedStateOf { pagerState.currentPage == iteration }
                                }
                                Indicator(
                                    isSelected = isSelected,
                                    selectedColor = MaterialTheme.colorScheme.primary,
                                    backgroundColor = MaterialTheme.colorScheme.background,
                                    defaultRadius = size_8_dp,
                                    selectedLength = size_24_dp
                                )

                                if (iteration < pageTextContent.size)
                                    Spacer(modifier = Modifier.width(size_4_dp))
                            }
                        }
                        Text(
                            modifier = Modifier
                                .clickable { onCreate() }
                                .constrainAs(btnSkip) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(btnNext.start, size_16_dp)
                                },
                            text = stringResource(id = R.string.action_skip),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = redHatDisplayFontFamily
                        )
                        Button(
                            shape = CircleShape,
                            modifier = Modifier
                                .size(60.dp)
                                .constrainAs(btnNext) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(parent.end, size_16_dp)
                                },
                            onClick = {
                                with(pagerState) {
                                    scrollCoroutineScope.launch {
                                        animateScrollToPage(currentPage + 1)
                                    }
                                }
                            }
                        ) {
                            Image(
                                modifier = Modifier
                                    .rotate(180f),
                                painter = painterResource(id = R.drawable.arrow_back),
                                contentDescription = "start",
                                alignment = Alignment.Center
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = isLastPage,
                    enter = slideInHorizontally(initialOffsetX = { it }),
                    exit = slideOutHorizontally(targetOffsetX = { it })
                ) {
                    Button(
                        modifier = Modifier
                            .padding(horizontal = size_16_dp)
                            .fillMaxWidth()
                            .height(size_48_dp),
                        onClick = onCreate
                    ) {
                        Text(
                            text = stringResource(id = R.string.action_start),
                            fontWeight = FontWeight.Bold,
                            fontFamily = redHatDisplayFontFamily
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun InfoPreview() {
    InfoScreen(
        onCreate = {}
    )
}