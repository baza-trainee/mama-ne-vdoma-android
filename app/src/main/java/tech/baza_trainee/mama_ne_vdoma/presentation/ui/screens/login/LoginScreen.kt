package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.getViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.PasswordTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.SocialLoginBlock
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.getTextWithUnderline
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.vm.LoginScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField

@Composable
fun LoginUserScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginScreenViewModel,
    onCreateUser: () -> Unit = {},
    onRestore: () -> Unit = {},
    onLogin: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.systemBars)
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val screenState = viewModel.viewState.collectAsStateWithLifecycle()

        Column(
            modifier = modifier
                .imePadding()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
            ) {
                Spacer(modifier = modifier.height(16.dp))

                Text(
                    modifier = modifier
                        .fillMaxWidth(),
                    text = "Увійти у свій профіль",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = modifier.height(24.dp))

                OutlinedTextFieldWithError(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    text = viewModel.email,
                    label = "Введіть свій email",
                    onValueChange = { viewModel.validateEmail(it) },
                    isError = screenState.value.emailValid == ValidField.INVALID,
                    errorText = "Ви ввели некоректний email",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null
                        )
                    }
                )

                Spacer(modifier = modifier.height(16.dp))

                PasswordTextFieldWithError(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    password = viewModel.password,
                    onValueChange = { viewModel.validatePassword(it) },
                    isError = screenState.value.passwordValid == ValidField.INVALID
                )

                Spacer(modifier = modifier.height(8.dp))

                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onRestore()
                        },
                    text = getTextWithUnderline("", "Забули пароль?", false),
                    textAlign = TextAlign.End,
                    fontSize = 14.sp,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = modifier.height(48.dp))

                Button(
                    modifier = modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = onLogin,
                    enabled = screenState.value.passwordValid == ValidField.VALID &&
                            screenState.value.emailValid == ValidField.VALID
                ) {
                    Text(
                        text = "Увійти",
                        fontFamily = redHatDisplayFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = modifier.height(32.dp))

//                ConstraintLayout(
//                    modifier = modifier.fillMaxWidth()
//                ) {
//                    val (box1, text, box2) = createRefs()
//                    Box(
//                        modifier = modifier
//                            .height(height = 2.dp)
//                            .background(color = Gray)
//                            .constrainAs(box1) {
//                                start.linkTo(parent.start, 24.dp)
//                                end.linkTo(text.start, 16.dp)
//                                top.linkTo(parent.top)
//                                bottom.linkTo(parent.bottom)
//                                width = Dimension.fillToConstraints
//                            }
//                    )
//                    Text(
//                        modifier = modifier
//                            .constrainAs(text) {
//                                start.linkTo(box1.end, 16.dp)
//                                end.linkTo(box2.start, 16.dp)
//                                top.linkTo(parent.top)
//                                bottom.linkTo(parent.bottom)
//                                width = Dimension.wrapContent
//                            },
//                        text = "чи",
//                        fontSize = 14.sp,
//                        fontFamily = redHatDisplayFontFamily
//                    )
//                    Box(
//                        modifier = modifier
//                            .height(height = 2.dp)
//                            .background(color = Gray)
//                            .constrainAs(box2) {
//                                start.linkTo(text.end, 16.dp)
//                                end.linkTo(parent.end, 24.dp)
//                                top.linkTo(parent.top)
//                                bottom.linkTo(parent.bottom)
//                                width = Dimension.fillToConstraints
//                            }
//                    )
//                }

                Spacer(modifier = modifier.height(32.dp))
            }

            SocialLoginBlock(
                modifier = modifier,
                horizontalPadding = 24.dp,
                getTextWithUnderline("Ще немає профілю? ", "Зареєструватись"),
                onCreateUser
            )
        }
    }
}

@Composable
@Preview
fun LoginUserPreview() {
    LoginUserScreen(
        viewModel = getViewModel()
    )
}