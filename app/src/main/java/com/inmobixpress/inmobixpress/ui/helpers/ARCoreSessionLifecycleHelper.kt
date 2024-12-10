package com.inmobixpress.inmobixpress.ui.helpers

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.exceptions.CameraNotAvailableException
import kotlinx.coroutines.launch


@Composable
fun DisposableEffectArCoreWithLifeCycle(
    permissionGranted: Boolean,
    onSessionCreated: (Session) -> Unit,
    onExceptionLaunched: (Exception) -> Unit,
    onResume: () -> Unit = { },
    onPause: () -> Unit = { },
    onDispose: () -> Unit = { }
) {
    var session: Session? = null
    val context = LocalContext.current
    // Safely update the current lambdas when a new one is provided
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val currentOnResume by rememberUpdatedState(onResume)
    val currentOnPause by rememberUpdatedState(onPause)
    val currentOnDispose by rememberUpdatedState(onDispose)
    val scope = rememberCoroutineScope()

    if (permissionGranted) {
        // If `lifecycleOwner` changes, dispose and reset the effect
        DisposableEffect(lifecycleOwner) {
            // Create an observer that triggers our remembered callbacks
            // for lifecycle events
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        Log.e("LC", "onCreate")
                    }
                    Lifecycle.Event.ON_START -> {
                        Log.e("LC", "onStart")
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        scope.launch {
                            val sessionCreated = session ?: tryCreateSession(
                                context = context,
                                permissionGranted = permissionGranted,
                                onExceptionLaunched = { exception ->
                                    onExceptionLaunched(exception)
                                }) ?: return@launch
                            try {
                                onSessionCreated(sessionCreated)
                                sessionCreated.resume()
                                session = sessionCreated
                            } catch (e: CameraNotAvailableException) {
                                onExceptionLaunched(e)
                            }
                        }
                        Log.e("LC", "onResume")
                        currentOnResume()
                    }
                    Lifecycle.Event.ON_PAUSE -> {
                        Log.e("LC", "onPause")
                        currentOnPause()
                    }
                    Lifecycle.Event.ON_STOP -> {
                        Log.e("LC", "onStop")
                    }
                    Lifecycle.Event.ON_DESTROY -> {
                        Log.e("LC", "onDestroy")
                    }
                    else -> {
                        Log.e("LC", "onAny")
                    }
                }
            }
            // Add the observer to the lifecycle
            lifecycleOwner.lifecycle.addObserver(observer)
            // When the effect leaves the Composition, remove the observer
            onDispose {
                session?.close()
                session = null
                Log.e("LC", "onDispose")
                currentOnDispose()
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
        Log.e("ARCORE", permissionGranted.toString())
    }
}

private fun tryCreateSession(
    context: Context,
    permissionGranted: Boolean,
    features: Set<Session.Feature> = setOf(),
    onExceptionLaunched: (Exception) -> Unit
): Session? {
    Log.e("ARCOREC", permissionGranted.toString())
    var installRequested = false
    // The app must have been given the CAMERA permission. If we don't have it yet, request it.
    if (permissionGranted.not()) {
        return null
    } else {
        return try {
            // Request installation if necessary.
            when (ArCoreApk.getInstance().requestInstall(context as Activity, installRequested.not())) {
                ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                    installRequested = true
                    // tryCreateSession will be called again, so we return null for now.
                    onExceptionLaunched(Exception("Install requested"))
                    Log.e("ARCORE", "Install requested")
                    return null
                }

                ArCoreApk.InstallStatus.INSTALLED -> {
                    // Left empty; nothing needs to be done.
                    Log.e("ARCORE", "Installed")
                }
            }
            // Create a session if Google Play Services for AR is installed and up to date.
            Session(context, features)
        } catch (e: Exception) {
            onExceptionLaunched(e)
            null
        }
    }
}

fun configureSession(session: Session) {
    session.configure(
        session.config.apply {
            // Enable Geospatial Mode.
            geospatialMode = Config.GeospatialMode.ENABLED
            // Enable Streetscape Geometry.
            streetscapeGeometryMode = Config.StreetscapeGeometryMode.ENABLED
        }
    )
}