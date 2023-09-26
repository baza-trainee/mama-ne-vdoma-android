package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun ChildInfoFunc(
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    ChildInfo(
        onNext = onNext,
        onBack = onBack
    )
}

@Composable
fun  ChildInfo(
    modifier: Modifier = Modifier,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Surface(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .fillMaxSize()
    ) {
        ConstraintLayout(
            modifier = modifier
                .imePadding()
                .fillMaxWidth()
        ) {
            val (topBar, content, btnNext) = createRefs()

            val topGuideline = createGuidelineFromTop(0.2f)

            Column(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                        bottom.linkTo(topGuideline)
                        height = Dimension.fillToConstraints
                    }
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                IconButton(
                    modifier = modifier
                        .padding(start = 16.dp, top = 16.dp)
                        .height(24.dp)
                        .width(24.dp),
                    onClick = { onBack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .padding(horizontal = 24.dp),
                    text = "Реєстрація пройшла успішно",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .constrainAs(content) {
                        top.linkTo(topGuideline)
                        bottom.linkTo(btnNext.top, 16.dp)
                        height = Dimension.fillToConstraints
                    },
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = modifier.height(16.dp))

                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 8.dp, top = 8.dp),
                    text = "Це допоможе підібрати для вас групи " +
                            "з дітьми приблизно одного віку",
                    textAlign = TextAlign.Start
                )
            }

            Button(
                modifier = modifier
                    .fillMaxWidth()
                    .constrainAs(btnNext) {
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    }
                    .padding(horizontal = 24.dp)
                    .height(48.dp),
                onClick = onNext
            ) {
                Text(text = "Далі")
            }
        }
    }
}

@Composable
@Preview
fun ChildInfoPreview() {
    ChildInfo(
        onBack = {},
        onNext = {}
    )
}
