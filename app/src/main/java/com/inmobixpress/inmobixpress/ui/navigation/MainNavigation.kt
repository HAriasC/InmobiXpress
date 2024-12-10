package com.inmobixpress.inmobixpress.ui.navigation

import android.app.Activity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.inmobixpress.inmobixpress.ui.viewmodel.MainViewModel
import com.inmobixpress.inmobixpress.ui.screens.DetailContainerScreen
import com.inmobixpress.inmobixpress.ui.screens.HomeScreen
import com.inmobixpress.inmobixpress.ui.screens.LiveScreen
import com.inmobixpress.inmobixpress.ui.screens.MapScreen
import com.inmobixpress.inmobixpress.ui.screens.ProfileScreen
import com.inmobixpress.inmobixpress.ui.screens.SearchScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainNavigation(
    viewModel: MainViewModel,
    navController: NavHostController,
    hostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    NavHost(navController = navController, startDestination = NavScreen.Home) {
        composable<NavScreen.Home>(
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up, tween(700)
                )
            },
            popExitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(700)
                )
            }
        ) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToDetail = { id ->
                    scope.launch {
                        viewModel.onVisibleChanged(false)
                        delay(timeMillis = 300)
                        viewModel.onVisibleContactBarChanged(true)
                        navController.navigate(NavScreen.Detail(id = id))
                    }
                },
                onNavigateToSearch = { id ->
                    navController.navigate(NavScreen.Search(id = id))
                }
            )
        }
        composable<NavScreen.Map>(
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up, tween(700)
                )
            },
            popExitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(700)
                )
            }
        ) {
            MapScreen(
                viewModel = viewModel,
                hostState = hostState,
                onNavigateToDetail = { id ->
                    scope.launch {
                        viewModel.onVisibleChanged(false)
                        delay(timeMillis = 300)
                        viewModel.onVisibleContactBarChanged(true)
                        navController.navigate(NavScreen.Detail(id = id))
                    }
                },
                onNavigateToSearch = { id ->
                    navController.navigate(NavScreen.Search(id = id))
                }
            )
        }
        composable<NavScreen.Live>(
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up, tween(700)
                )
            },
            popExitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(700)
                )
            }
        ) {
            val context = LocalContext.current
            LiveScreen(
                viewModel = viewModel,
                hostState = hostState,
                onNavigateToDetail = { id ->
                    viewModel.onVisibleChanged(false)
                    viewModel.onVisibleContactBarChanged(true)
                    navController.navigate(NavScreen.Detail(id = id))
                },
                onNavigateBack = {
                    scope.launch {
                        viewModel.onVisibleChanged(true)
                        delay(timeMillis = 200)
                        navController.navigateUp()
                    }
                }
            )
        }
        composable<NavScreen.Search>(
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up, tween(700)
                )
            },
            popExitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(700)
                )
            }
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<NavScreen.Search>()
            SearchScreen(
                id = args.id,
                onNavigateBack = { id ->
                    when (id) {
                        0 -> navController.navigate(NavScreen.Home)
                        1 -> navController.navigate(NavScreen.Map)
                    }
                }
            )
        }
        composable<NavScreen.Profile>(
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up, tween(700)
                )
            },
            popExitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(700)
                )
            }
        ) {
            ProfileScreen()
        }
        composable<NavScreen.Detail>(
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up, tween(700)
                )
            },
            popExitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(700)
                )
            }
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<NavScreen.Detail>()
            DetailContainerScreen(
                viewModel = viewModel,
                id = args.id,
                onNavigateBack = {
                    scope.launch {
                        viewModel.onVisibleContactBarChanged(false)
                        delay(timeMillis = 200)
                        viewModel.onVisibleChanged(true)
                        delay(timeMillis = 200)
                        navController.navigateUp()
                    }
                }
            )
        }
    }
}

fun toggleFullScreenEvent(hide: Boolean, activity: Activity) {
    val windowInsetsController =
        WindowCompat.getInsetsController(activity.window, activity.window.decorView)
    // Configure the behavior of the hidden system bars.
    windowInsetsController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

    // Add a listener to update the behavior of the toggle fullscreen button when
    // the system bars are hidden or revealed.
    ViewCompat.setOnApplyWindowInsetsListener(activity.window.decorView) { view, windowInsets ->
        // You can hide the caption bar even when the other system bars are visible.
        // To account for this, explicitly check the visibility of navigationBars()
        // and statusBars() rather than checking the visibility of systemBars().
        if (windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
            || windowInsets.isVisible(WindowInsetsCompat.Type.statusBars())
        ) {
            if (hide) {
                // Hide both the status bar and the navigation bar.
                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            }
        } else {
            if (hide.not()) {
                // Show both the status bar and the navigation bar.
                windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
            }
        }
        ViewCompat.onApplyWindowInsets(view, windowInsets)
    }
}

fun toggleFullScreen(hide: Boolean, activity: Activity) {
    val windowInsetsController = WindowCompat.getInsetsController(
        activity.window, activity.window.decorView
    )
    windowInsetsController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    if (hide) {
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    } else {
        windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
    }
}