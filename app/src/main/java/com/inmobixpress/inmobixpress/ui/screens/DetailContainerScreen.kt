package com.inmobixpress.inmobixpress.ui.screens

import android.os.Build
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Whatsapp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.libraries.places.api.Places
import com.inmobixpress.inmobixpress.data.network.implement.LoginServiceImpl
import com.inmobixpress.inmobixpress.data.network.implement.PropertyServiceImpl
import com.inmobixpress.inmobixpress.repository.LoginRepository
import com.inmobixpress.inmobixpress.repository.PropertyRepository
import com.inmobixpress.inmobixpress.ui.viewmodel.MainViewModel
import com.inmobixpress.inmobixpress.ui.components.ContactBottomSheet
import com.inmobixpress.inmobixpress.ui.components.CustomTab
import com.inmobixpress.inmobixpress.ui.components.TopBar
import com.inmobixpress.inmobixpress.ui.components.WhatsAppBottomSheet
import com.inmobixpress.inmobixpress.ui.model.PropertyItem
import com.inmobixpress.inmobixpress.ui.utils.callProprietor
import com.inmobixpress.inmobixpress.ui.utils.previewDetailTabList
import com.inmobixpress.inmobixpress.ui.viewmodel.LoginViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailContainerScreen(
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel,
    id: Int,
    onNavigateToLogin: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    val visible: Boolean by mainViewModel.contactBottomBarVisible.observeAsState(true)

    BackPressHandler(onBackPressed = onNavigateBack)
    val properties by mainViewModel.propertyItems.observeAsState(emptyMap())
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showWhatsAppBottomSheet by mainViewModel.whatsappBottomSheetVisible.observeAsState()
    val showContactBottomSheet by mainViewModel.contactBottomSheetVisible.observeAsState()
    Scaffold(
        topBar = {
            TopBar(
                title = "Departamento en alquiler",
                visible = true,
                enableBackAction = true,
                onNavigateBack = onNavigateBack
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
            ) {
                ContactBottomBar(
                    viewModel = mainViewModel,
                    sheetState = sheetState,
                    property = properties[id]!!
                )
            }
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(
            TopAppBarDefaults.windowInsets.union(NavigationBarDefaults.windowInsets)
        )
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            CustomTab(
                viewModel = mainViewModel,
                items = previewDetailTabList(
                    viewModel = mainViewModel,
                    property = properties[id]!!
                ),
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                mainViewModel.serviceMarkers.clear()
                Places.deinitialize()
            }
        }
        if (showWhatsAppBottomSheet == true) {
            WhatsAppBottomSheet(
                viewModel = mainViewModel,
                property = properties[id]!!,
                onNavigateToLogin = onNavigateToLogin
            )
        }
        if (showContactBottomSheet == true) {
            ContactBottomSheet(
                mainViewModel = mainViewModel,
                loginViewModel = loginViewModel,
                sheetState = sheetState,
                property = properties[id]!!,
                onNavigateToLogin = onNavigateToLogin
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactBottomBar(viewModel: MainViewModel, sheetState: SheetState, property: PropertyItem) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            OutlinedIconButton(onClick = {
                context.callProprietor(property)
            }) {
                Icon(
                    imageVector = Icons.Outlined.Phone,
                    contentDescription = "",
                )
            }
            Button(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .weight(1.0.toFloat()),
                onClick = {
                    viewModel.onWhatsAppBottomSheetVisible(true)
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Whatsapp,
                    contentDescription = "",
                )
                Text(modifier = Modifier.padding(start = 4.dp), text = "WhatsApp")
            }
            OutlinedButton(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1.0.toFloat()),
                onClick = {
                    viewModel.onContactBottomSheetVisible(true)
                    scope.launch {
                        sheetState.expand()
                    }
                }) {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "",
                )
                Text(modifier = Modifier.padding(start = 4.dp), text = "Contactar")
            }
        }
    }
}

@Composable
fun BackPressHandler(
    backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit,
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DetailContainerScreenPreview() {
    DetailContainerScreen(
        mainViewModel = MainViewModel(
            PropertyRepository(
                PropertyServiceImpl(
                    HttpClient()
                )
            )
        ),
        loginViewModel = LoginViewModel(
            LoginRepository(
                LoginServiceImpl(
                    HttpClient()
                )
            )
        ),
        id = 0,
        onNavigateToLogin = {},
        onNavigateBack = {}
    )
}