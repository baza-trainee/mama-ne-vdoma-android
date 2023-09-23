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
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Gray
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.Mama_ne_vdomaTheme
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ShowHidePasswordTextField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.SocialLoginBlock
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.getTextWithUnderline

@Composable
fun CreateUserFunc(
    onCreateUser: () -> Unit,
    onLogin: () -> Unit
) {
    CreateUser(
        onCreateUser = onCreateUser,
        onLogin = onLogin
    )
}

@Composable
fun CreateUser(
    modifier: Modifier = Modifier,
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

                    val emailText = remember { mutableStateOf(TextFieldValue("")) }

                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        value = emailText.value,
                        label = { Text("Введіть свій email") },
                        onValueChange = { newValue ->
                            emailText.value = newValue
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Gray,
                            unfocusedContainerColor = Gray,
                            disabledContainerColor = Gray,
                            focusedBorderColor = MaterialTheme.colorScheme.background,
                            unfocusedBorderColor = MaterialTheme.colorScheme.background,
                        )
                    )

                    Spacer(modifier = modifier.height(16.dp))

                    ShowHidePasswordTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        label = "Введіть свій пароль",
                        placeHolder = "Пароль"
                    )

                    Spacer(modifier = modifier.height(8.dp))

                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        text = "Ваш пароль повинен складатись з 6-12 символів і включати хоча б одну цифру",
                        fontSize = 14.sp,
                    )

                    Spacer(modifier = modifier.height(16.dp))

                    ShowHidePasswordTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        label = "Повторіть ваш пароль",
                        placeHolder = "Пароль"
                    )

                    Spacer(modifier = modifier.height(24.dp))

                    Button(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(horizontal = 24.dp),
                        onClick = onCreateUser
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