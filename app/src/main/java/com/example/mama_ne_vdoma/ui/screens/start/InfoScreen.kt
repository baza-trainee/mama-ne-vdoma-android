package com.example.mama_ne_vdoma.ui.screens.start

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.mama_ne_vdoma.R
import com.example.mama_ne_vdoma.ui.theme.Mama_ne_vdomaTheme
import com.example.mama_ne_vdoma.utils.Indicator
import kotlinx.coroutines.launch

@Composable
fun InfoScreenFunc(onCreate: () -> Unit) {
    InfoScreen(onCreate = onCreate)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfoScreen(
    modifier: Modifier = Modifier,
    onCreate: () -> Unit = {}
) {

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
                val (pager, spacer, footer) = createRefs()

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .constrainAs(pager) {
                            top.linkTo(parent.top)
                            height = Dimension.wrapContent
                        }
                        .fillMaxWidth()
                ) { page ->
                    Column(
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Image(
                            modifier = modifier
                                .fillMaxWidth(),
                            alignment = Alignment.TopCenter,
                            painter = painterResource(id = pagerImageContent[page]),
                            contentDescription = pageTextContent[page],
                            contentScale = ContentScale.FillWidth
                        )
                        Spacer(
                            modifier = modifier.height(16.dp)
                        )
                        Text(
                            text = "Няня у твоєму телефоні",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(
                            modifier = modifier.height(8.dp)
                        )
                        Text(
                            text = pageTextContent[page],
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(
                    modifier = modifier
                        .constrainAs(spacer) {
                            top.linkTo(pager.bottom, 8.dp)
                            bottom.linkTo(footer.top, 8.dp)
                        }
                        .height(16.dp)
                )

                ConstraintLayout(
                    modifier = modifier
                        .fillMaxWidth()
                        .constrainAs(footer) {
                            bottom.linkTo(parent.bottom, 16.dp)
                            height = Dimension.preferredValue(70.dp)
                        }
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    if (pagerState.currentPage < 2) {
                        val (indicator, btnSkip, btnNext) = createRefs()

                        Row(
                            modifier = modifier
                                .constrainAs(indicator) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                }
                                .padding(horizontal = 16.dp)
                        ) {
                            repeat(pageTextContent.size) { iteration ->
                                val isSelected by remember {
                                    derivedStateOf { pagerState.currentPage == iteration }
                                }
                                Indicator(
                                    isSelected = isSelected,
                                    selectedColor = MaterialTheme.colorScheme.primary,
                                    backgroundColor = MaterialTheme.colorScheme.background,
                                    defaultRadius = 8.dp,
                                    selectedLength = 24.dp,
                                    modifier = modifier.padding(horizontal = 4.dp)
                                )
                            }
                        }
                        Text(
                            modifier = modifier
                                .clickable {
                                    onCreate()
                                }
                                .constrainAs(btnSkip) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(btnNext.start, 24.dp)
                                },
                            text = "Пропустити"
                        )
                        Button(
                            shape = CircleShape,
                            modifier = modifier
                                .size(60.dp)
                                .constrainAs(btnNext) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(parent.end, 16.dp)
                                },
                            onClick = {
                                with(pagerState) {
                                    scrollCoroutineScope.launch {
                                        animateScrollToPage(currentPage + 1)
                                    }
                                }
                            }
                        ) {
                            Text(text = ">", fontSize = 24.sp)
                        }
                    } else {
                        Button(
                            modifier = modifier
                                .height(48.dp)
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            onClick = onCreate
                        ) {
                            Text(text = "Почати")
                        }
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