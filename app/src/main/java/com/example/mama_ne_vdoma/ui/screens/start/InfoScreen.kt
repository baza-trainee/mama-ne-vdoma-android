package com.example.mama_ne_vdoma.ui.screens.start

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.mama_ne_vdoma.R
import com.example.mama_ne_vdoma.ui.theme.Mama_ne_vdomaTheme
import com.example.mama_ne_vdoma.utils.CustomButton
import kotlinx.coroutines.launch

@Composable
fun InfoScreenFunc(onCreate: () -> Unit) {
    InfoScreen(onCreate = onCreate)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfoScreen(modifier: Modifier = Modifier, onCreate: () -> Unit = {}) {

    val scrollCoroutineScope = rememberCoroutineScope()

    val pageTextContent = listOf(
        "Тобі треба йти на роботу? Ти шукаєш дитячий садочок для своїх дітей, але більшість недоступні для тебе або занадто дорогі?",
        "Ми домоможемо тобі знайти поруч мам з такими ж проблемами! Організуйте своїх дітей у дитячі групи та по черзі доглядайте за ними у вільний час!",
        "Насолоджуйся своєю роботою, поки твої діти щасливі та у безпеці!"
    )

    val pagerImageContent = listOf(
        R.drawable.info_1,
        R.drawable.info_2,
        R.drawable.info_3
    )

    val pagerState = rememberPagerState(pageCount = { pageTextContent.size })

    Mama_ne_vdomaTheme {
        Surface(
            modifier = modifier.fillMaxSize()
        ) {
            ConstraintLayout(
                modifier = modifier.fillMaxSize()
            ) {
                val (pager, footer) = createRefs()

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .constrainAs(pager) {
                            top.linkTo(parent.top)
                            bottom.linkTo(footer.top, margin = 16.dp)
                            height = Dimension.fillToConstraints
                        }
                        .fillMaxWidth()
                ) { page ->
                    Column(
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Image(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(
                                    bottom = 16.dp
                                ),
                            painter = painterResource(id = pagerImageContent[page]),
                            contentDescription = pageTextContent[page],
                            contentScale = ContentScale.FillWidth
                        )
                        Text(
                            text = "Няня у твоєму телефоні",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(
                                    bottom = 16.dp
                                ),
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = pageTextContent[page],
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = modifier
                                .fillMaxWidth(),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                val (indicator, btnSkip, btnNext) = createRefs()

                ConstraintLayout(
                    modifier = modifier
                        .fillMaxWidth()
                        .constrainAs(footer) {
                            bottom.linkTo(parent.bottom, margin = 16.dp)
                        }
                        .wrapContentHeight()
                        .padding(bottom = 16.dp)
                ) {
                    if (pagerState.currentPage < 2) {
                        Row(
                            modifier = modifier
                                .wrapContentHeight()
                                .constrainAs(indicator) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                }
                                .padding(horizontal = 8.dp)
                        ) {
                            repeat(pageTextContent.size) { iteration ->
                                Indicator(
                                    isSelected = pagerState.currentPage == iteration,
                                    selectedColor = MaterialTheme.colorScheme.primary,
                                    backgroundColor = MaterialTheme.colorScheme.background,
                                    defaultRadius = 8.dp,
                                    selectedLength = 24.dp,
                                    modifier = modifier.padding(horizontal = 4.dp)
                                )
                            }
                        }
                        CustomButton(
                            modifier = modifier
                                .constrainAs(btnSkip) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(btnNext.start)
                            }
                                .height(48.dp),
                            text = "Пропустити",
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            action = onCreate
                        )
                        CustomButton(
                            text = ">",
                            fontSize = 24.sp,
                            shape = CircleShape,
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor =MaterialTheme.colorScheme.onPrimary,
                            modifier = modifier
                                .size(60.dp)
                                .constrainAs(btnNext) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(parent.end, 16.dp)
                                }
                        ) {
                            with(pagerState) {
                                scrollCoroutineScope.launch {
                                    animateScrollToPage(currentPage + 1)
                                }
                            }
                        }
                    } else {
                        CustomButton(
                            modifier = modifier
                                .height(48.dp)
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                            ,
                            text = "Почати",
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            action = onCreate
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
    InfoScreen()
}

@Composable
fun Indicator(
    isSelected: Boolean,
    selectedColor: Color,
    backgroundColor: Color,
    defaultRadius: Dp,
    selectedLength: Dp,
    modifier: Modifier = Modifier
) {
    val width by animateDpAsState(
        targetValue = if (isSelected) selectedLength else defaultRadius,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = ""
    )
    Box(
        modifier = modifier
            .height(defaultRadius)
            .width(width)
            .clip(CircleShape)
            .border(if (isSelected) 0.dp else 2.dp, selectedColor)
            .background(color = if (isSelected) selectedColor else backgroundColor)
    )
}