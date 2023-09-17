package com.example.mama_ne_vdoma.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.example.mama_ne_vdoma.utils.ShowHidePasswordTextField
import com.example.mama_ne_vdoma.utils.SocialLoginBlock
import com.example.mama_ne_vdoma.utils.getTextWithUnderline

@Composable
fun LoginUserFunc(
    onCreateUser: () -> Unit,
    onRestore: () -> Unit,
    onGoogleLogin: () -> Unit,
    onFBLogin: () -> Unit,
    onLogin: () -> Unit
) {
    LoginUser(
        onCreateUser = onCreateUser,
        onRestore = onRestore,
        onGoogleLogin = onGoogleLogin,
        onFBLogin = onFBLogin,
        onLogin = onLogin,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginUser(
    modifier: Modifier = Modifier,
    onCreateUser: () -> Unit = {},
    onRestore: () -> Unit = {},
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
                        text = "Увійти у свій профіль",
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
                            .padding(horizontal = 16.dp)
                            .clickable {
                                onRestore()
                            },
                        text = getTextWithUnderline("", "Забули пароль?", false),
                        textAlign = TextAlign.End,
                        fontSize = 14.sp,
                    )

                    Spacer(modifier = modifier.height(48.dp))

                    Button(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(horizontal = 16.dp),
                        onClick = onLogin
                    ) {
                        Text(text = "Увійти")
                    }

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
                    getTextWithUnderline("Ще немає профілю? ", "Зареєструватись"),
                    onGoogleLogin,
                    onFBLogin,
                    onCreateUser
                )
            }
        }
    }
}

@Composable
@Preview
fun LoginUserPreview() {
    LoginUser()
}