package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.palm.composestateevents.EventEffect
import org.koin.androidx.compose.getViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.PasswordTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.SocialLoginBlock
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.SurfaceWithSystemBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.getTextWithUnderline
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm.UserCreateViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun CreateUserScreen(
    modifier: Modifier = Modifier,
    viewModel: UserCreateViewModel,
    onCreateUser: () -> Unit = {},
    onLogin: () -> Unit = {}
) {
    SurfaceWithSystemBars(
        modifier = modifier
    ) {
        val screenState = viewModel.userCreateViewState.collectAsStateWithLifecycle()

        val context = LocalContext.current

        EventEffect(
            event = screenState.value.registerSuccess,
            onConsumed = {}
        ) { onCreateUser() }

        EventEffect(
            event = screenState.value.registerError,
            onConsumed = { viewModel.consumeRegisterError() }
        ) { if (it.isNotBlank()) Toast.makeText(context, it, Toast.LENGTH_LONG).show() }

        val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .verticalScroll(scrollState)
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

                Spacer(modifier = modifier.height(2.dp))

                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    text =
                    "Ваш пароль повинен складатись з 6-24 символів і обов’язково містити великі та малі латинські букви, цифри, спеціальні знаки",
                    fontSize = 14.sp,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = modifier.height(16.dp))

                PasswordTextFieldWithError(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    label = "Повторіть ваш пароль",
                    password = viewModel.confirmPassword,
                    onValueChange = { viewModel.validateConfirmPassword(it) },
                    isError = screenState.value.confirmPasswordValid == ValidField.INVALID,
                    errorText = "Паролі не співпадають"
                )

                Spacer(modifier = modifier.height(8.dp))

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = screenState.value.isPolicyChecked,
                        onCheckedChange = { viewModel.updatePolicyCheck(it) }
                    )
                    Text(
                        modifier = modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        text = getTextWithUnderline(
                            "Даю згоду на використання моїх даних згідно з ",
                            "Політикою конфіденційності"
                        ),
                        fontSize = 14.sp,
                        fontFamily = redHatDisplayFontFamily
                    )
                }

                Spacer(modifier = modifier.height(24.dp))

                Button(
                    modifier = modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = {
//                        onCreateUser() //for test
                        viewModel.registerUser()
                    },
                    enabled = screenState.value.isAllConform
                ) {
                    ButtonText(
                        text = "Зареєструватись"
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
                getTextWithUnderline("Вже є акаунт? ", "Увійти"),
                onLogin
            )
        }

        if (screenState.value.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun CreateUserPreview() {
    CreateUserScreen(
        viewModel = getViewModel()
    )
}