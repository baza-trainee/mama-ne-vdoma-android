package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.model.UserCreateViewState
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm.UserCreateViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Gray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Mama_ne_vdomaTheme
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.PasswordTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.SocialLoginBlock
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.getTextWithUnderline

@Composable
fun CreateUserFunc(
    viewModel: UserCreateViewModel,
    onCreateUser: () -> Unit,
    onLogin: () -> Unit
) {
    CreateUser(
        screenState = viewModel.viewState.collectAsStateWithLifecycle(),
        validateEmail = { viewModel.validateEmail(it) },
        validatePassword = { viewModel.validatePassword(it) },
        validateConfirmPassword = { viewModel.validateConfirmPassword(it) },
        onCreateUser = onCreateUser,
        onLogin = onLogin
    )
}

@Composable
fun CreateUser(
    modifier: Modifier = Modifier,
    screenState: State<UserCreateViewState> = mutableStateOf(UserCreateViewState()),
    validateEmail: (String) -> Unit = {},
    validatePassword: (String) -> Unit = {},
    validateConfirmPassword: (String) -> Unit = {},
    onCreateUser: () -> Unit = {},
    onLogin: () -> Unit = {}
) {
    Mama_ne_vdomaTheme {
        Surface(
            modifier = modifier
                .windowInsetsPadding(WindowInsets.systemBars)
                .fillMaxSize()
        ) {
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
                        text = "Створити профіль",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = modifier.height(24.dp))

                    OutlinedTextFieldWithError(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        text = screenState.value.email,
                        label = "Введіть свій email",
                        onValueChange = { validateEmail(it) },
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
                        password = screenState.value.password,
                        onValueChange = { validatePassword(it) },
                        isError = screenState.value.passwordValid == ValidField.INVALID
                    )

                    Spacer(modifier = modifier.height(16.dp))

                    PasswordTextFieldWithError(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        label = "Повторіть ваш пароль",
                        password = screenState.value.confirmPassword,
                        onValueChange = { validateConfirmPassword(it) },
                        isError = screenState.value.confirmPasswordValid == ValidField.INVALID,
                        errorText = "Паролі не співпадають"
                    )

                    Spacer(modifier = modifier.height(24.dp))

                    Button(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(horizontal = 24.dp),
                        onClick = onCreateUser,
                        enabled = screenState.value.emailValid == ValidField.VALID &&
                                screenState.value.passwordValid == ValidField.VALID &&
                                screenState.value.confirmPasswordValid == ValidField.VALID
                    ) {
                        Text(text = "Зареєструватись")
                    }

                    Spacer(modifier = modifier.height(8.dp))

                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        text = getTextWithUnderline(
                            "Натискаючи цю кнопку, ви даєте згоду на використання ваших даних згідно з ",
                            "Політикою конфіденційності"
                        ),
                        fontSize = 14.sp,
                    )

                    Spacer(modifier = modifier.height(32.dp))

                    ConstraintLayout(
                        modifier = modifier.fillMaxWidth()
                    ) {
                        val (box1, text, box2) = createRefs()
                        Box(
                            modifier = modifier
                                .height(height = 2.dp)
                                .background(color = Gray)
                                .constrainAs(box1) {
                                    start.linkTo(parent.start, 24.dp)
                                    end.linkTo(text.start, 16.dp)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    width = Dimension.fillToConstraints
                                }
                        )
                        Text(
                            modifier = modifier
                                .constrainAs(text) {
                                    start.linkTo(box1.end, 16.dp)
                                    end.linkTo(box2.start, 16.dp)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    width = Dimension.wrapContent
                                },
                            text = "чи",
                            fontSize = 14.sp,
                        )
                        Box(
                            modifier = modifier
                                .height(height = 2.dp)
                                .background(color = Gray)
                                .constrainAs(box2) {
                                    start.linkTo(text.end, 16.dp)
                                    end.linkTo(parent.end, 24.dp)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    width = Dimension.fillToConstraints
                                }
                        )
                    }

                    Spacer(modifier = modifier.height(32.dp))
                }

                SocialLoginBlock(
                    modifier = modifier,
                    horizontalPadding = 24.dp,
                    getTextWithUnderline("Вже є акаунт? ", "Увійти"),
                    onLogin
                )
            }
        }
    }
}

@Composable
@Preview
fun CreateUserPreview() {
    CreateUser()
}