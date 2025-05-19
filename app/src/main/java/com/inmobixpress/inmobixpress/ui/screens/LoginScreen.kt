package com.inmobixpress.inmobixpress.ui.screens

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MapsHomeWork
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inmobixpress.inmobixpress.R
import com.inmobixpress.inmobixpress.data.network.implement.LoginServiceImpl
import com.inmobixpress.inmobixpress.data.network.model.User
import com.inmobixpress.inmobixpress.repository.LoginRepository
import com.inmobixpress.inmobixpress.ui.components.LoadingScreen
import com.inmobixpress.inmobixpress.ui.components.MessageDialog
import com.inmobixpress.inmobixpress.ui.model.UIState
import com.inmobixpress.inmobixpress.ui.theme.InmobiXpressAppTheme
import com.inmobixpress.inmobixpress.ui.viewmodel.LoginViewModel
import io.ktor.client.HttpClient

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToMain: (User) -> Unit,
) {
    val showLoading by viewModel.loadingVisible.observeAsState(initial = false)
    val showErrorDialog by viewModel.errorDialogVisible.observeAsState(initial = false)
    val result by viewModel.result.collectAsState()

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            TopSection()
            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)
            ) {
                LoginSection(viewModel = viewModel)
                Spacer(modifier = Modifier.height(30.dp))
                SocialMediaSection()

                val uiColor = if (isSystemInDarkTheme()) White else Black
                Box(
                    modifier = Modifier
                        .fillMaxHeight(fraction = 0.8f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color(0xFF94A3B8),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            ) {
                                append("¿No tienes una cuenta?")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = uiColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            ) {
                                append(" ")
                                append("Registrate ahora")
                            }
                        },
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = showLoading,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
        ) {
            LoadingScreen(message = "Iniciando sesión")
        }

        when {
            showErrorDialog -> {
                MessageDialog(
                    onDismissRequest = {
                        viewModel.onErrorDialogVisible(visible = false)
                        viewModel.reset()
                    },
                    onConfirmation = {
                        viewModel.onErrorDialogVisible(visible = false)
                        viewModel.reset()
                    },
                    dialogTitle = "Lo sentimos, ocurrió un error",
                    dialogText = (result as UIState.Error<User>).error.message.toString(),
                    icon = Icons.Default.Warning,
                    isError = true,
                    confirmationText = "Entendido"
                )
            }
        }

        when (result) {
            is UIState.Loading -> viewModel.onLoadingVisible(visible = true)

            is UIState.Success -> {
                onNavigateToMain((result as UIState.Success<User>).data)
                viewModel.clearForm()
                viewModel.reset()
                viewModel.onLoadingVisible(visible = false)
            }

            is UIState.Error -> {
                viewModel.onLoadingVisible(visible = false)
                viewModel.onErrorDialogVisible(visible = true)
            }

            is UIState.None -> {}
        }
    }
}

@Composable
private fun SocialMediaSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "`O´ continúe con",
            style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF64748B))
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SocialMediaLogIn(
                icon = R.drawable.google,
                text = "Google",
                modifier = Modifier.weight(1f)
            ) {

            }
            Spacer(modifier = Modifier.width(20.dp))
            SocialMediaLogIn(
                icon = R.drawable.facebook,
                text = "Facebook",
                modifier = Modifier.weight(1f)
            ) {

            }
        }
    }
}

@Composable
private fun LoginSection(viewModel: LoginViewModel) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val username by viewModel.username.observeAsState(initial = "")
    val usernameError by viewModel.usernameError.observeAsState(initial = false)
    val usernameMessageError by viewModel.usernameMessageError.observeAsState(initial = "")
    val password by viewModel.password.observeAsState(initial = "")
    val passwordError by viewModel.passwordError.observeAsState(initial = false)
    val passwordMessageError by viewModel.passwordMessageError.observeAsState(initial = "")

    LoginTextField(
        label = "Nombre de usuario",
        trailing = "",
        modifier = Modifier.fillMaxWidth(),
        text = username,
        error = usernameError,
        errorMessage = usernameMessageError,
        onValueChange = {
            viewModel.onUsernameChanged(it)
            viewModel.validateForm()
        },
        onKeyboardActions = {
            viewModel.validateForm()
        },
        onFocusChanged = {
            focusManager.moveFocus(FocusDirection.Next)
        }
    )
    Spacer(modifier = Modifier.height(15.dp))
    LoginTextField(
        label = "Contraseña",
        trailing = "¿Olvidaste tu contraseña?",
        modifier = Modifier.fillMaxWidth(),
        text = password,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        error = passwordError,
        errorMessage = passwordMessageError,
        isPassword = true,
        onValueChange = {
            viewModel.onPasswordChanged(it)
            viewModel.validateForm()
        },
        onKeyboardActions = {
            viewModel.validateForm()
        },
        onFocusChanged = {
            focusManager.clearFocus()
            keyboardController?.hide()
        },
        visualTransformation = PasswordVisualTransformation()
    )
    Spacer(modifier = Modifier.height(20.dp))
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        onClick = {
            if (viewModel.validateForm()) {
                viewModel.onLoadingVisible(visible = true)
                viewModel.login()
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSystemInDarkTheme()) Color(0xFF334155) else Black,
            contentColor = White
        ),
        shape = RoundedCornerShape(size = 4.dp)
    ) {
        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
private fun TopSection() {
    val uiColor = if (isSystemInDarkTheme()) White else Black

    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.5f),
            painter = painterResource(id = R.drawable.shape),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )


        Row(
            modifier = Modifier.padding(top = 70.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.MapsHomeWork,
                contentDescription = "",
                modifier = Modifier
                    .size(60.dp)
                    .fillMaxSize(1.0F),
                tint = if (isSystemInDarkTheme()) White else Black
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = uiColor
                )
                Text(
                    text = stringResource(id = R.string.app_slogan),
                    style = MaterialTheme.typography.titleMedium,
                    color = uiColor
                )
            }
        }

        Text(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .align(alignment = Alignment.BottomCenter),
            text = stringResource(id = R.string.login),
            style = MaterialTheme.typography.headlineLarge,
            color = uiColor
        )
    }
}

@Composable
fun LoginTextField(
    modifier: Modifier = Modifier,
    label: String,
    trailing: String,
    text: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    ),
    error: Boolean,
    errorMessage: String,
    onValueChange: (String) -> Unit,
    onKeyboardActions: () -> Unit,
    onFocusChanged: () -> Unit,
    isPassword: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val uiColor = if (isSystemInDarkTheme()) White else Black
    val showPassword = rememberSaveable() { mutableStateOf(false) }
    OutlinedTextField(
        modifier = modifier.semantics {
            if (error) error(message = errorMessage)
        },
        value = text,
        onValueChange = onValueChange,
        label = {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = uiColor)
        },
        supportingText = {
            Row {
                Text(
                    text = if (error) errorMessage else "",
                    modifier = Modifier.clearAndSetSemantics { }
                )
            }
        },
        isError = error,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions {
            onKeyboardActions.invoke()
            onFocusChanged.invoke()
        },
        colors = TextFieldDefaults.colors(
            unfocusedPlaceholderColor = if (isSystemInDarkTheme()) Color(0xFF94A3B8) else Color(
                0xFF475569
            ),
            focusedPlaceholderColor = if (isSystemInDarkTheme()) White else Black,
            unfocusedContainerColor = if (isSystemInDarkTheme()) Color(0xFF334155)
                .copy(alpha = 0.6f) else Color(0xFFF1F5F9),
            focusedContainerColor = if (isSystemInDarkTheme()) Color(0xFF334155)
                .copy(alpha = 0.6f) else Color(0xFFF1F5F9),
        ),
        trailingIcon = {
            if (isPassword) {
                val icon = if (showPassword.value) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }

                IconButton(onClick = { showPassword.value = !showPassword.value }) {
                    Icon(
                        icon,
                        contentDescription = "Visibility"
                    )
                }
            } else {
                TextButton(onClick = { /*TODO*/ }) {
                    Text(
                        text = trailing,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                        color = uiColor
                    )
                }
            }
        },
        visualTransformation = if (isPassword) {
            if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation()
        } else visualTransformation
    )
}

@Composable
fun SocialMediaLogIn(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    text: String,
    onClick: () -> Unit,
) {

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .socialMedia()
            .clickable { onClick() }
            .height(40.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF64748B))
        )

    }

}


fun Modifier.socialMedia(): Modifier = composed {
    if (isSystemInDarkTheme()) {
        background(Color.Transparent).border(
            width = 1.dp,
            color = Color(0xFF334155),
            shape = RoundedCornerShape(4.dp)
        )
    } else {
        background(Color(0xFFF1F5F9))
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginPreview() {
    InmobiXpressAppTheme {
        LoginScreen(
            viewModel = LoginViewModel(LoginRepository(LoginServiceImpl(HttpClient()))),
            onNavigateToMain = {}
        )
    }
}