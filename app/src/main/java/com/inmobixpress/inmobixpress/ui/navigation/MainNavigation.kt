package com.inmobixpress.inmobixpress.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.inmobixpress.inmobixpress.ui.screens.DetailContainerScreen
import com.inmobixpress.inmobixpress.ui.screens.HomeScreen
import com.inmobixpress.inmobixpress.ui.screens.LiveScreen
import com.inmobixpress.inmobixpress.ui.screens.MapScreen
import com.inmobixpress.inmobixpress.ui.screens.ProfileScreen
import com.inmobixpress.inmobixpress.ui.screens.SearchScreen

@Composable
fun MainNavigation(
    navController: NavHostController
) {
    AnimatedVisibility(visible = true) {

        NavHost(navController = navController, startDestination = NavScreen.Home) {
            composable<NavScreen.Home> {
                HomeScreen(
                    onNavigateToDetail = { id ->
                        navController.navigate(NavScreen.Detail(id = id))
                    }
                )
            }
            composable<NavScreen.Map> {
                MapScreen(
                    onNavigateToDetail = { id ->
                        navController.navigate(NavScreen.Detail(id = id))
                    }
                )
            }
            composable<NavScreen.Live> {
                LiveScreen(
                    onNavigateToDetail = { id ->
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
                    id = args.id,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}