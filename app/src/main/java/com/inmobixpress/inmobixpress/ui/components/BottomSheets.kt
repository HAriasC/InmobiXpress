package com.inmobixpress.inmobixpress.ui.components

import android.app.PendingIntent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Whatsapp
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.inmobixpress.inmobixpress.ui.viewmodel.MainViewModel
import com.inmobixpress.inmobixpress.ui.model.PropertyItem
import com.inmobixpress.inmobixpress.ui.screens.Message
import com.inmobixpress.inmobixpress.ui.utils.previewContactTabList
import com.inmobixpress.inmobixpress.ui.utils.previewListProperty
import com.inmobixpress.inmobixpress.ui.utils.sendWhatsAppsProprietor
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.inmobixpress.inmobixpress.R
import com.inmobixpress.inmobixpress.data.network.implement.PropertyServiceImpl
import com.inmobixpress.inmobixpress.repository.PropertyRepository
import com.inmobixpress.inmobixpress.ui.screens.ContactBar
import com.inmobixpress.inmobixpress.ui.screens.NearbyMarkers
import com.inmobixpress.inmobixpress.ui.screens.Phone
import com.inmobixpress.inmobixpress.ui.screens.RequestBar
import com.inmobixpress.inmobixpress.ui.theme.PurpleGrey40
import io.ktor.client.HttpClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactBottomSheet(viewModel: MainViewModel, sheetState: SheetState, property: PropertyItem) {
    ModalBottomSheet(
        onDismissRequest = {
            viewModel.onContactBottomSheetVisible(false)
            viewModel.clearForm()
        },
        sheetState = sheetState
    ) {
        val tabIndex by viewModel.tabIndex.observeAsState()
        val showConfirmDialog by viewModel.confirmDialogVisible.observeAsState()
        val showRequestDialog by viewModel.requestDialogVisible.observeAsState()
        val showErrorDialog by viewModel.errorDialogVisible.observeAsState()
        val showCompleteDialog by viewModel.completeDialogVisible.observeAsState()
        val showVisitDialog by viewModel.visitDialogVisible.observeAsState()
        val compositionComplete by rememberLottieComposition(
            spec = LottieCompositionSpec.Url(
                "https://lottie.host/65fc8803-19e9-40f4-bf32-fd3cef3c1992/SG99vgf37z.json"
            )
        )
        val compositionVisit by rememberLottieComposition(
            spec = LottieCompositionSpec.Url(
                "https://lottie.host/6dd80fd8-d45e-4fce-8817-854359f76bb9/L2dsFnWTGz.json"
            )
        )
        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.BottomEnd
        ) {
            ContactContainer(viewModel = viewModel, sheetState = sheetState, property = property)
            androidx.compose.animation.AnimatedVisibility(visible = tabIndex == 0) {
                ContactBar(viewModel = viewModel, property = property, modifier = Modifier.offset {
                    IntOffset(
                        x = 0,
                        y = -sheetState
                            .requireOffset()
                            .toInt()
                    )
                })
            }
            androidx.compose.animation.AnimatedVisibility(visible = tabIndex == 1) {
                RequestBar(viewModel = viewModel, property = property, modifier = Modifier.offset {
                    IntOffset(
                        x = 0,
                        y = -sheetState
                            .requireOffset()
                            .toInt()
                    )
                })
            }
        }
        when {
            showConfirmDialog == true -> {
                FormAlertDialog(
                    onDismissRequest = { viewModel.onConfirmDialogVisible(false) },
                    onConfirmation = {
                        viewModel.onConfirmDialogVisible(false)
                        viewModel.onCompleteDialogVisible(true)
                    },
                    dialogTitle = "Información validada",
                    dialogText = "Revisa tus datos antes de enviar el mensaje.",
                    icon = Icons.Default.CheckCircleOutline,
                    isError = false,
                    confirmationText = "Enviar"
                )
            }

            showRequestDialog == true -> {
                FormAlertDialog(
                    onDismissRequest = { viewModel.onRequestDialogVisible(false) },
                    onConfirmation = {
                        viewModel.onRequestDialogVisible(false)
                        viewModel.onVisitDialogVisible(true)
                    },
                    dialogTitle = "Información validada",
                    dialogText = "Revisa tus datos antes de solicitar la visita.",
                    icon = Icons.Default.CheckCircleOutline,
                    isError = false,
                    confirmationText = "Solicitar"
                )
            }

            showErrorDialog == true -> {
                FormAlertDialog(
                    onDismissRequest = { viewModel.onErrorDialogVisible(false) },
                    onConfirmation = { viewModel.onErrorDialogVisible(false) },
                    dialogTitle = "Faltan algunos datos",
                    dialogText = "Completa el formulario correctamente.",
                    icon = Icons.Default.Info,
                    isError = true,
                    confirmationText = "Entendido"
                )
            }

            showCompleteDialog == true -> {
                DialogWithAnimation(
                    dialogTitle = "¡Su mensaje fue enviado exitosamente!",
                    onDismissRequest = {
                        viewModel.onCompleteDialogVisible(false)
                        viewModel.onContactBottomSheetVisible(false)
                        viewModel.clearForm()
                    },
                    onConfirmation = {
                        viewModel.onCompleteDialogVisible(false)
                        viewModel.onContactBottomSheetVisible(false)
                        viewModel.clearForm()
                    },
                    composition = compositionComplete,
                    isVisit = false
                )
            }

            showVisitDialog == true -> {
                DialogWithAnimation(
                    dialogTitle = "¡Su visita fue agendada exitosamente!",
                    onDismissRequest = {
                        viewModel.onVisitDialogVisible(false)
                        viewModel.onContactBottomSheetVisible(false)
                        viewModel.clearForm()
                    },
                    onConfirmation = {
                        viewModel.onVisitDialogVisible(false)
                        viewModel.onContactBottomSheetVisible(false)
                        viewModel.clearForm()
                    },
                    composition = compositionVisit,
                    isVisit = true
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactContainer(viewModel: MainViewModel, sheetState: SheetState, property: PropertyItem) {
    CustomTab(
        viewModel = viewModel,
        items = previewContactTabList(
            viewModel = viewModel,
            sheetState = sheetState,
            property = property
        ),
        modifier = Modifier.padding(top = 10.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatsAppBottomSheet(viewModel: MainViewModel, property: PropertyItem) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    ModalBottomSheet(
        onDismissRequest = {
            viewModel.onWhatsAppBottomSheetVisible(false)
            viewModel.clearForm()
        },
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Phone,
                    contentDescription = "",
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 6.dp)
                )
                Phone(viewModel = viewModel) {
                    focusManager.moveFocus(FocusDirection.Next)
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

                    }
                ) {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            WhatsAppBar(viewModel = viewModel, property = property)
        }
    }
}

@Composable
fun WhatsAppBar(viewModel: MainViewModel, property: PropertyItem) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            onClick = {
                if (viewModel.validateWhatsApp()) {
                    context.sendWhatsAppsProprietor(
                        property = property,
                        message = viewModel.message.value.toString()
                    )
                    viewModel.onWhatsAppBottomSheetVisible(false)
                    viewModel.clearForm()
                }
            }) {
            Icon(
                imageVector = Icons.Outlined.Whatsapp,
                contentDescription = "",
            )
            Text(modifier = Modifier.padding(start = 4.dp), text = "WhatsApp")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapTrackerBottomSheet(
    viewModel: MainViewModel,
    cameraPositionState: CameraPositionState,
    markerState: MarkerState,
    rotationMarker: MutableFloatState,
    onNavigateToDetail: (id: Int) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = {
            viewModel.onTrackerBottomSheetVisible(false)
            viewModel.clearForm()
        },
        sheetState = rememberModalBottomSheetState()
    ) {
        Log.e("GPS", "${rotationMarker.floatValue} ${markerState.position}")
        val properties by remember {
            mutableStateOf(
                MapProperties(
                    mapType = MapType.TERRAIN
                )
            )
        }
        val uiSettings by remember {
            mutableStateOf(
                MapUiSettings(
                    rotationGesturesEnabled = false,
                    zoomControlsEnabled = false
                )
            )
        }
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 200.dp),
                    cameraPositionState = cameraPositionState,
                    properties = properties,
                    uiSettings = uiSettings
                ) {
                    NearbyMarkers(
                        keyedLocationMarker = viewModel.nearbyProperties.toList(),
                        onNavigateToDetail = onNavigateToDetail
                    )
                    MarkerComposable(
                        state = markerState,
                        anchor = Offset(0.45f, 0.5f),
                        rotation = rotationMarker.floatValue
                    ) {
                        Icon(
                            bitmap = ImageBitmap.imageResource(id = R.drawable.ic_navigation_white_48dp),
                            contentDescription = "",
                            modifier = Modifier.scale(0.5f, 0.5f),
                            tint = PurpleGrey40
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PhoneNumberConsent(

) {
    val context = LocalContext.current
    val request = GetPhoneNumberHintIntentRequest.builder().build()
    val phoneNumberHintIntentResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            try {
                val phoneNumber =
                    Identity.getSignInClient(context).getPhoneNumberFromIntent(result.data)
                Log.e("TAG", phoneNumber)
            } catch (e: Exception) {
                Log.e("TAG", "Phone Number Hint failed $result")
            }
        }

    LaunchedEffect(Unit) {
        Identity.getSignInClient(context)
            .getPhoneNumberHintIntent(request)
            .addOnSuccessListener { result: PendingIntent ->
                try {
                    phoneNumberHintIntentResultLauncher.launch(
                        IntentSenderRequest.Builder(result).build()
                    )
                } catch (e: Exception) {
                    Log.e("TAG", "Launching the PendingIntent failed")
                }
            }
            .addOnFailureListener {
                Log.e("TAG", "Phone Number Hint failed")
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ContactBottomSheetPreview() {
    ContactContainer(
        viewModel = MainViewModel(
            PropertyRepository(
                PropertyServiceImpl(
                    HttpClient()
                )
            )
        ),
        sheetState = rememberModalBottomSheetState(),
        previewListProperty()[0]
    )
}

@Preview
@Composable
fun WhatsAppBottomSheetPreview() {
    WhatsAppBottomSheet(
        viewModel = MainViewModel(
            PropertyRepository(
                PropertyServiceImpl(
                    HttpClient()
                )
            )
        ),
        property = previewListProperty()[0]
    )
}

