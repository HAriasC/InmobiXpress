package com.inmobixpress.inmobixpress.ui.components

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.inmobixpress.inmobixpress.data.network.implement.PropertyServiceImpl
import com.inmobixpress.inmobixpress.repository.PropertyRepository
import com.inmobixpress.inmobixpress.ui.viewmodel.MainViewModel
import com.inmobixpress.inmobixpress.ui.utils.dateToString
import com.inmobixpress.inmobixpress.ui.utils.hourToMillis
import com.inmobixpress.inmobixpress.ui.utils.millisToLocalDateTime
import com.inmobixpress.inmobixpress.ui.utils.today
import com.inmobixpress.inmobixpress.ui.utils.validateDayOfWeek
import com.inmobixpress.inmobixpress.ui.utils.year
import io.ktor.client.HttpClient
import kotlinx.datetime.toKotlinLocalDateTime

@Composable
fun FormAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    isError: Boolean,
    confirmationText: String
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText, textAlign = TextAlign.Center)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            if (isError) {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text(text = confirmationText)
                }
            } else {
                TextButton(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text(text = confirmationText)
                }
            }
        },
        dismissButton = {
            if (isError.not()) {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Cancelar")
                }
            }
        }
    )
}

@Composable
fun DialogWithAnimation(
    dialogTitle: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    composition: LottieComposition?,
    isVisit: Boolean
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LottieAnimation(
                    composition = composition,
                    modifier = Modifier
                        .height(200.dp)
                        .width(200.dp)
                        .padding(top = 26.dp, start = if (isVisit) 26.dp else 0.dp),
                    iterations = LottieConstants.IterateForever,
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = dialogTitle,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        modifier = Modifier
                            .padding(bottom = 26.dp),
                        onClick = onConfirmation
                    ) {
                        Text(text = "Volver")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerWithDialog(
    viewModel: MainViewModel,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    val dateState = rememberDatePickerState(
        selectableDates =
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return validateDayOfWeek(utcTimeMillis)
            }

            override fun isSelectableYear(year: Int): Boolean {
                return year >= year()
            }
        }
    )
    val dateToString = dateState.selectedDateMillis?.let {
        dateToString(it.plus(hourToMillis(hours = 5)))
    } ?: today()
    DatePickerDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.onVisitDayChanged(dateToString)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        viewModel.onVisitLocalChanged(
                            visitLocal = dateState.selectedDateMillis?.plus(hourToMillis(hours = 5))
                                .millisToLocalDateTime()
                                .toKotlinLocalDateTime()
                        )
                        viewModel.onVisitMillisChanged(
                            visitMillis = dateState.selectedDateMillis!!.plus(
                                hourToMillis(hours = 5)
                            )
                        )
                    }
                    onConfirmation()
                }
            ) {
                Text(text = "Confirmar")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismissRequest() }
            ) {
                Text(text = "Cancelar")
            }
        }
    ) {
        DatePicker(
            state = dateState,
            showModeToggle = true
        )
    }
}

@Composable
fun MessageDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    isError: Boolean,
    confirmationText: String,
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = icon, contentDescription = "", modifier = Modifier
                    .size(60.dp)
                    .fillMaxSize(1.0F)
            )
        },
        title = {
            Text(text = dialogTitle, fontSize = 20.sp)
        },
        text = {
            Text(text = dialogText, textAlign = TextAlign.Center)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            if (isError) {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text(text = confirmationText)
                }
            } else {
                TextButton(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text(text = confirmationText)
                }
            }
        }
    )
}

@Preview
@Composable
fun AlertDialogPreview() {
    FormAlertDialog(
        onDismissRequest = { },
        onConfirmation = { },
        dialogTitle = "Faltan algunos datos",
        dialogText = "Completa el formulario correctamente.",
        icon = Icons.Default.Info,
        isError = true,
        confirmationText = "Entendido"
    )
}

@Preview
@Composable
fun DialogAnimatedPreview() {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Url(
            "https://lottie.host/65fc8803-19e9-40f4-bf32-fd3cef3c1992/SG99vgf37z.json"
        )
    )
    DialogWithAnimation(
        dialogTitle = "¡Tu mensaje fue enviado exitosamente!",
        onDismissRequest = { },
        onConfirmation = { },
        composition = composition,
        false
    )
}

@Preview
@Composable
fun DatePickerWithDialogPreview() {
    DatePickerWithDialog(
        viewModel = MainViewModel(
            PropertyRepository(
                PropertyServiceImpl(
                    HttpClient()
                )
            )
        ),
        onDismissRequest = { },
        onConfirmation = { }
    )
}