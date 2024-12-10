package com.inmobixpress.inmobixpress.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person2
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.PhotoCameraFront
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inmobixpress.inmobixpress.data.network.implement.PropertyServiceImpl
import com.inmobixpress.inmobixpress.repository.PropertyRepository
import com.inmobixpress.inmobixpress.ui.viewmodel.MainViewModel
import com.inmobixpress.inmobixpress.ui.model.PropertyItem
import com.inmobixpress.inmobixpress.ui.utils.previewListProperty
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(viewModel: MainViewModel, sheetState: SheetState, property: PropertyItem) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val isVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val scrollState = rememberScrollState()
    LaunchedEffect(isVisible) {
        if (isVisible) {
            sheetState.expand()
        } else {
            sheetState.expand()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Person2,
                    contentDescription = "",
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
                )
                Name(viewModel = viewModel) {
                    focusManager.moveFocus(FocusDirection.Next)
                    scope.launch {
                        sheetState.expand()
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "",
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
                )
                Email(viewModel = viewModel) {
                    focusManager.moveFocus(FocusDirection.Next)
                    scope.launch {
                        sheetState.expand()
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Phone,
                    contentDescription = "",
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
                )
                Phone(viewModel = viewModel) {
                    focusManager.moveFocus(FocusDirection.Next)
                    scope.launch {
                        scrollState.scrollTo(scrollState.maxValue)
                        sheetState.expand()
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.PhotoCameraFront,
                    contentDescription = "",
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
                )
                DNI(viewModel = viewModel) {
                    focusManager.moveFocus(FocusDirection.Next)
                    scope.launch {
                        scrollState.scrollTo(scrollState.maxValue)
                        sheetState.expand()
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Message,
                    contentDescription = "",
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
                )
                Message(
                    viewModel = viewModel,
                    onValueChange = {
                        if (it > 3) {
                            scope.launch {
                                scrollState.scrollTo(scrollState.maxValue)
                            }
                        }
                    }
                ) {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    scope.launch {
                        sheetState.expand()
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(90.dp))
    }
}

@Composable
fun Name(viewModel: MainViewModel, onFocusChanged: () -> Unit) {
    val name by viewModel.name.observeAsState(initial = "")
    val nameError by viewModel.nameError.observeAsState(initial = false)
    val nameMessageError by viewModel.nameMessageError.observeAsState(initial = "")
    OutlinedTextField(
        value = name,
        onValueChange = {
            viewModel.onNameChanged(it)
            viewModel.validateName()
        },
        singleLine = true,
        label = { Text("Nombre completo") },
        supportingText = {
            Row {
                Text(
                    text = if (nameError) nameMessageError else "",
                    modifier = Modifier.clearAndSetSemantics { }
                )
            }
        },
        isError = nameError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        keyboardActions = KeyboardActions {
            viewModel.validateName()
            onFocusChanged.invoke()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, end = 8.dp)
            .semantics {
                if (nameError) error(message = nameMessageError)
            }
    )
}

@Composable
fun Email(viewModel: MainViewModel, onFocusChanged: () -> Unit) {
    val email by viewModel.email.observeAsState(initial = "")
    val emailError by viewModel.emailError.observeAsState(initial = false)
    val emailMessageError by viewModel.emailMessageError.observeAsState(initial = "")
    OutlinedTextField(
        value = email,
        onValueChange = {
            viewModel.onEmailChanged(it)
            viewModel.validateEmail()
        },
        singleLine = true,
        label = { Text("Correo electrónico") },
        supportingText = {
            Row {
                Text(
                    text = if (emailError) emailMessageError else "",
                    modifier = Modifier.clearAndSetSemantics { }
                )
            }
        },
        isError = emailError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        keyboardActions = KeyboardActions {
            viewModel.validateEmail()
            onFocusChanged.invoke()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, end = 8.dp)
            .semantics {
                if (emailError) error(message = emailMessageError)
            }
    )
}

@Composable
fun Phone(viewModel: MainViewModel, onFocusChanged: () -> Unit) {
    val phone by viewModel.phone.observeAsState(initial = "")
    val phoneError by viewModel.phoneError.observeAsState(initial = false)
    val phoneMessageError by viewModel.phoneMessageError.observeAsState(initial = "")
    OutlinedTextField(
        value = phone,
        onValueChange = {
            viewModel.onPhoneChanged(it)
            viewModel.validatePhone()
        },
        singleLine = true,
        label = { Text("Número telefónico") },
        supportingText = {
            Row {
                Text(
                    text = if (phoneError) phoneMessageError else "",
                    modifier = Modifier.clearAndSetSemantics { }
                )
            }
        },
        isError = phoneError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        keyboardActions = KeyboardActions {
            viewModel.validatePhone()
            onFocusChanged.invoke()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, end = 8.dp)
            .semantics {
                if (phoneError) error(message = phoneMessageError)
            }
    )
}

@Composable
fun DNI(viewModel: MainViewModel, onFocusChanged: () -> Unit) {
    val dni by viewModel.dni.observeAsState(initial = "")
    val dniError by viewModel.dniError.observeAsState(initial = false)
    val dniMessageError by viewModel.dniMessageError.observeAsState(initial = "")
    OutlinedTextField(
        value = dni,
        onValueChange = {
            viewModel.onDniChanged(it)
            viewModel.validateDNI()
        },
        singleLine = true,
        label = { Text("Documento de identidad") },
        supportingText = {
            Row {
                Text(
                    text = if (dniError) dniMessageError else "",
                    modifier = Modifier.clearAndSetSemantics { }
                )
            }
        },
        isError = dniError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        keyboardActions = KeyboardActions {
            viewModel.validateDNI()
            onFocusChanged.invoke()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, end = 8.dp)
            .semantics {
                if (dniError) error(message = dniMessageError)
            }
    )
}

@Composable
fun Message(viewModel: MainViewModel, onValueChange: (Int) -> Unit, onFocusChanged: () -> Unit) {
    val message by viewModel.message.observeAsState(initial = "")
    val messageError by viewModel.messageError.observeAsState(initial = false)
    val messageTextError by viewModel.messageTextError.observeAsState(initial = "")
    val charLimit = 2000
    OutlinedTextField(
        value = message,
        onValueChange = {
            viewModel.onMessageChanged(it)
            viewModel.validateMessage()
            onValueChange(it.length)
        },
        label = { Text("Mensaje") },
        supportingText = {
            Row {
                Text(
                    text = if (messageError) messageTextError else "",
                    modifier = Modifier.clearAndSetSemantics { }
                )
                Spacer(Modifier.weight(1f))
                Text("Caracteres: ${message.length}/$charLimit")
            }
        },
        isError = messageError,
        keyboardActions = KeyboardActions {
            viewModel.validateMessage()
            onFocusChanged.invoke()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, end = 8.dp)
            .semantics {
                if (messageError) error(message = messageTextError)
            }
    )
}

@Composable
fun ContactBar(viewModel: MainViewModel, property: PropertyItem, modifier: Modifier) {
    Column(
        modifier = modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            onClick = {
                viewModel.validateForm()
            }) {
            Icon(
                imageVector = Icons.Outlined.Email,
                contentDescription = "",
            )
            Text(modifier = Modifier.padding(start = 4.dp), text = "Contactar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ContactScreenPreview() {
    ContactScreen(
        viewModel = MainViewModel(
            PropertyRepository(
                PropertyServiceImpl(
                    HttpClient()
                )
            )
        ),
        sheetState = rememberModalBottomSheetState(),
        property = previewListProperty()[0]
    )
}