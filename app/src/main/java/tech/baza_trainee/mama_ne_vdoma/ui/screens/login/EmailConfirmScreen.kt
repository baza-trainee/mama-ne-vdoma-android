package tech.baza_trainee.mama_ne_vdoma.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.ui.theme.Mama_ne_vdomaTheme
import tech.baza_trainee.mama_ne_vdoma.utils.getTextWithUnderline

@Composable
fun EmailConfirmFunc(
    onLogin: () -> Unit,
    onSendAgain: () -> Unit
) {
    EmailConfirm(
        onLogin = onLogin,
        onSendAgain = onSendAgain
    )
}

@Composable
fun EmailConfirm(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit,
    onSendAgain: () -> Unit
) {
    Mama_ne_vdomaTheme {
        Surface(
            modifier = modifier
                .windowInsetsPadding(WindowInsets.navigationBars)
                .fillMaxSize()
        ) {
            ConstraintLayout(
                modifier = modifier.fillMaxWidth()
            ) {
                val (title, image, btnStart, btnLogin) = createRefs()

                val topGuideline = createGuidelineFromTop(0.2f)

                Column(
                    modifier = modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .constrainAs(title) {
                            top.linkTo(parent.top)
                            bottom.linkTo(topGuideline)
                            height = Dimension.fillToConstraints
                        }
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        text = "Лист був відправлений",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(bottom = 8.dp),
                        text = "Перевірте свою пошту email@gmail.com, " +
                                "щоб отримати подальші інструкції з " +
                                "відновлення паролю",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Image(
                    modifier = modifier
                        .constrainAs(image) {
                            top.linkTo(topGuideline)
                            bottom.linkTo(btnStart.top, 64.dp)
                            height = Dimension.fillToConstraints
                        }
                        .fillMaxWidth(),
                    painter = painterResource(id = R.drawable.email_confirm),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight
                )

                Button(
                    modifier = modifier
                        .constrainAs(btnStart) {
                            bottom.linkTo(btnLogin.top, margin = 16.dp)
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(48.dp),
                    onClick = onLogin
                ) {
                    Text(text = "Увійти")
                }
                Text(
                    text = getTextWithUnderline("Не отримали листа? ", "Відправити ще раз"),
                    modifier = modifier
                        .constrainAs(btnLogin) {
                            bottom.linkTo(parent.bottom, margin = 16.dp)
                        }
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onSendAgain()
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(48.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
@Preview
fun EmailConfirmPreview() {
    EmailConfirm(
        onLogin = {},
        onSendAgain = {}
    )
}
