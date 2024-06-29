package com.inmobixpress.inmobixpress.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.inmobixpress.inmobixpress.MainViewModel
import com.inmobixpress.inmobixpress.ui.screens.DetailContainerScreen
import com.inmobixpress.inmobixpress.ui.screens.HomeScreen
import com.inmobixpress.inmobixpress.ui.screens.LiveScreen
import com.inmobixpress.inmobixpress.ui.screens.MapScreen
import com.inmobixpress.inmobixpress.ui.screens.ProfileScreen
import com.inmobixpress.inmobixpress.ui.screens.SearchScreen

@Composable
fun MainNavigation(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = NavScreen.Home) {
        composable<NavScreen.Home> {
            HomeScreen(
                onNavigateToDetail = { id ->
                    viewModel.onVisibleChanged(false)
                    viewModel.onVisibleContactBarChanged(true)
                    navController.navigate(NavScreen.Detail(id = id))
                }
            )
        }
        composable<NavScreen.Map> {
            MapScreen(
                onNavigateToDetail = { id ->
                    viewModel.onVisibleChanged(false)
                    viewModel.onVisibleContactBarChanged(true)
                    navController.navigate(NavScreen.Detail(id = id))
                }
            )
        }
        composable<NavScreen.Live> {
            LiveScreen(
                onNavigateToDetail = { id ->
                    viewModel.onVisibleChanged(false)
                    viewModel.onVisibleContactBarChanged(true)
                    navController.navigate(NavScreen.Detail(id = id))
                }
            )
        }
        composable<NavScreen.Search> {
            SearchScreen()
        }
        composable<NavScreen.Profile> {
            ProfileScreen()
        }
        composable<NavScreen.Detail> { backStackEntry ->
            val args = backStackEntry.toRoute<NavScreen.Detail>()
            DetailContainerScreen(
                viewModel = viewModel,
                id = args.id,
                onNavigateBack = {
                    viewModel.onVisibleContactBarChanged(false)
                    viewModel.onVisibleChanged(true)
                    navController.popBackStack()
                }
            )
        }
    }
}