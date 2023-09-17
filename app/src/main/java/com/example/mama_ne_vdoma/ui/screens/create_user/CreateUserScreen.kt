package com.example.mama_ne_vdoma.ui.screens.create_user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.mama_ne_vdoma.ui.theme.Gray
import com.example.mama_ne_vdoma.ui.theme.Mama_ne_vdomaTheme
import com.example.mama_ne_vdoma.utils.CustomButton
import com.example.mama_ne_vdoma.utils.ShowHidePasswordTextField
import com.example.mama_ne_vdoma.utils.SocialLoginBlock
import com.example.mama_ne_vdoma.utils.getTextWithUnderline

@Composable
fun CreateUserFunc(
    onCreateUser: () -> Unit,
    onGoogleLogin: () -> Unit,
    onFBLogin: () -> Unit,
    onLogin: () -> Unit
) {
    CreateUser(
        onCreateUser = onCreateUser,
        onGoogleLogin = onGoogleLogin,
        onFBLogin = onFBLogin,
        onLogin = onLogin
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUser(
    modifier: Modifier = Modifier,
    onCreateUser: () -> Unit = {},
    onGoogleLogin: () -> Unit = {},
    onFBLogin: () -> Unit = {},
    onLogin: () -> Unit = {}
) {
    Mama_ne_vdomaTheme {
        Surface(
            modifier = modifier.fillMaxSize()
        ) {
            Column(
                modifier = modifier
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

                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        modifier = modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        label = { Text("Введіть свій email") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Gray,
                            focusedBorderColor = MaterialTheme.colorScheme.background,
                            unfocusedBorderColor = MaterialTheme.colorScheme.background,
                        )
                    )

                    Spacer(modifier = modifier.height(16.dp))

                    ShowHidePasswordTextField(
                        modifier = modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        label = "Введіть свій пароль",
                        placeHolder = "Пароль"
                    )

                    Spacer(modifier = modifier.height(8.dp))

                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        text = "Ваш пароль повинен складатись з 6-12 символів і включати хоча б одну цифру",
                        fontSize = 14.sp,
                    )

                    Spacer(modifier = modifier.height(16.dp))

                    ShowHidePasswordTextField(
                        modifier = modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        label = "Повторіть ваш пароль",
                        placeHolder = "Пароль"
                    )

                    Spacer(modifier = modifier.height(24.dp))

                    CustomButton(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(horizontal = 16.dp),
                        text = "Зареєструватись",
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        action = onCreateUser
                    )

                    Spacer(modifier = modifier.height(8.dp))

                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
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
                                    start.linkTo(parent.start, 16.dp)
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
                                    end.linkTo(parent.end, 16.dp)
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
                    horizontalPadding = 16.dp,
                    getTextWithUnderline("Вже є акаунт? ", "Увійти"),
                    onGoogleLogin,
                    onFBLogin,
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