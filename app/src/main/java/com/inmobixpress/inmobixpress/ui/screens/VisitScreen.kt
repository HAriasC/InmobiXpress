package com.inmobixpress.inmobixpress.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LowPriority
import androidx.compose.material.icons.outlined.Person2
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.PhotoCameraFront
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inmobixpress.inmobixpress.data.network.implement.PropertyServiceImpl
import com.inmobixpress.inmobixpress.repository.PropertyRepository
import com.inmobixpress.inmobixpress.ui.viewmodel.MainViewModel
import com.inmobixpress.inmobixpress.ui.components.DatePickerWithDialog
import com.inmobixpress.inmobixpress.ui.model.PropertyItem
import com.inmobixpress.inmobixpress.ui.utils.previewListProperty
import com.inmobixpress.inmobixpress.ui.utils.today
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisitScreen(viewModel: MainViewModel, sheetState: SheetState, property: PropertyItem) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val isVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val showVisitDateDialog by viewModel.visitDayDialogVisible.observeAsState()
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
                    imageVector = Icons.Outlined.Today,
                    contentDescription = "",
                    modifier = Modifier.padding(start = 8.dp, top = 16.dp, end = 8.dp)
                )
                VisitDate(viewModel = viewModel) {
                    viewModel.onVisitDayDialogVisible(visible = true)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = "",
                    modifier = Modifier.padding(start = 8.dp, top = 16.dp, end = 8.dp)
                )
                TimeTable(viewModel = viewModel)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LowPriority,
                    contentDescription = "",
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp, end = 8.dp)
                )
                Priority(viewModel = viewModel)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Person2,
                    contentDescription = "",
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 2.dp)
                )
                Name(viewModel = viewModel) {
                    focusManager.moveFocus(FocusDirection.Next)
                    scope.launch {
                        scrollState.scrollTo(scrollState.maxValue)
                        sheetState.expand()
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "",
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 6.dp)
                )
                Email(viewModel = viewModel) {
                    focusManager.moveFocus(FocusDirection.Next)
                    scope.launch {
                        scrollState.scrollTo(scrollState.maxValue)
                        sheetState.expand()
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Phone,
                    contentDescription = "",
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 6.dp)
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
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 6.dp)
                )
                DNI(viewModel = viewModel) {
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
    if (showVisitDateDialog == true) {
        DatePickerWithDialog(viewModel = viewModel, onDismissRequest = {
            viewModel.onVisitDayDialogVisible(visible = false)
        }) {
            viewModel.onVisitDayDialogVisible(visible = false)
        }
    }
}

@Composable
fun VisitDate(viewModel: MainViewModel, onClick: () -> Unit) {
    val visitDay by viewModel.visitDay.observeAsState(
        initial = today()
    )
    Box {
        OutlinedTextField(
            value = visitDay,
            onValueChange = {
                viewModel.onVisitDayChanged(it)
            },
            enabled = false,
            readOnly = true,
            singleLine = true,
            label = {
                Text(
                    text = "Fecha",
                    modifier = Modifier.clickable(onClick = onClick)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, end = 8.dp, bottom = 8.dp)
                .clickable(onClick = onClick),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                //For Icons
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeTable(viewModel: MainViewModel) {
    val timeTable by viewModel.timeTable.observeAsState(initial = viewModel.timeTables()[0])
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            value = timeTable,
            onValueChange = {},
            readOnly = true,
            label = { Text("Horario") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
                .padding(top = 16.dp, end = 8.dp, bottom = 8.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            viewModel.timeTables().forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        viewModel.onTimeTableChanged(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Priority(viewModel: MainViewModel) {
    val priorityType by viewModel.priorityType.observeAsState(initial = viewModel.priotityTypes()[0])
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            value = priorityType,
            onValueChange = {},
            readOnly = true,
            label = { Text("Prioridad") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
                .padding(top = 16.dp, end = 8.dp, bottom = 16.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            viewModel.priotityTypes().forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        viewModel.onPriorityTypeChanged(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun RequestBar(viewModel: MainViewModel, property: PropertyItem, modifier: Modifier) {
    Column(
        modifier = modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Button(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .weight(1.0.toFloat()),
                onClick = {
                    viewModel.validateRequest()
                }) {
                Icon(
                    imageVector = Icons.Outlined.Today,
                    contentDescription = "",
                )
                Text(modifier = Modifier.padding(start = 4.dp), text = "Solicita tu visita")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun VisitScreenPreview() {
    VisitScreen(
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