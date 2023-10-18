package tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.LoadingIndicator
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.PrivacyPolicyBlock
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SocialLoginBlock
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.custom_views.SurfaceWithSystemBars
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.getTextWithUnderline
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.OutlinedTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.text_fields.PasswordTextFieldWithError
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.theme.redHatDisplayFontFamily
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.RequestState
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.ValidField
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.ButtonText

@Composable
fun CreateUserScreen(
    modifier: Modifier = Modifier,
    screenState: State<UserCreateViewState> = mutableStateOf(UserCreateViewState()),
    uiState: State<RequestState> = mutableStateOf(RequestState.Idle),
    handleEvent: (UserCreateEvent) -> Unit = { _ -> }
) {
    SurfaceWithSystemBars {
        BackHandler { handleEvent(UserCreateEvent.OnBack) }

        val context = LocalContext.current

        when(val state = uiState.value) {
            RequestState.Idle -> Unit
            is RequestState.OnError -> {
                if (state.error.isNotBlank()) Toast.makeText(
                    context,
                    state.error,
                    Toast.LENGTH_LONG
                ).show()
                handleEvent(UserCreateEvent.ResetUiState)
            }
        }

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .imePadding()
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Створити профіль",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = redHatDisplayFontFamily
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextFieldWithError(
                    modifier = Modifier.fillMaxWidth(),
                    text = screenState.value.email,
                    label = "Введіть свій email",
                    onValueChange = { handleEvent(UserCreateEvent.ValidateEmail(it)) },
                    isError = screenState.value.emailValid == ValidField.INVALID,
                    errorText = "Ви ввели некоректний email",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordTextFieldWithError(
                    modifier = Modifier.fillMaxWidth(),
                    password = screenState.value.password,
                    onValueChange = { handleEvent(UserCreateEvent.ValidatePassword(it)) },
                    isError = screenState.value.passwordValid == ValidField.INVALID
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text =
                    "Ваш пароль повинен складатись з 6-24 символів і обов’язково містити великі та малі латинські букви, цифри, спеціальні знаки",
                    fontSize = 14.sp,
                    fontFamily = redHatDisplayFontFamily,
                    style = TextStyle(lineHeight = 18.sp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordTextFieldWithError(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Повторіть ваш пароль",
                    password = screenState.value.confirmPassword,
                    onValueChange = { handleEvent(UserCreateEvent.ValidateConfirmPassword(it)) },
                    isError = screenState.value.confirmPasswordValid == ValidField.INVALID,
                    errorText = "Паролі не співпадають"
                )

                Spacer(modifier = Modifier.height(8.dp))

                PrivacyPolicyBlock(
                    modifier = Modifier.fillMaxWidth(),
                    isChecked = screenState.value.isPolicyChecked,
                    onCheckedChanged = { handleEvent(UserCreateEvent.UpdatePolicyCheck(it)) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = {
                        handleEvent(UserCreateEvent.RegisterUser)
                    },
                    enabled = screenState.value.isAllConform
                ) {
                    ButtonText(
                        text = "Зареєструватись"
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

//                ConstraintLayout(
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    val (box1, text, box2) = createRefs()
//                    Box(
//                        modifier = Modifier
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
//                        modifier = Modifier
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
//                        modifier = Modifier
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

                Spacer(modifier = Modifier.height(32.dp))
            }

            SocialLoginBlock(
                textForBottomButton = getTextWithUnderline("Вже є акаунт? ", "Увійти")
            ) { handleEvent(UserCreateEvent.OnLogin) }
        }

        if (screenState.value.isLoading) LoadingIndicator()
    }
}

@Composable
@Preview
fun CreateUserPreview() {
    CreateUserScreen()
}