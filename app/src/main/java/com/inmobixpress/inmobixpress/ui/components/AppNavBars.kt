package com.inmobixpress.inmobixpress.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.inmobixpress.inmobixpress.MainViewModel
import com.inmobixpress.inmobixpress.ui.model.BottomNavItem

@ExperimentalMaterial3Api
@Composable
fun TopBar(
    title: String,
    visible: Boolean,
    enableBackAction: Boolean,
    onNavigateBack: () -> Unit
) {
    AnimatedVisibility(visible = visible) {
        CenterAlignedTopAppBar(
            title = { Text(text = title, fontSize = 20.sp) },
            expandedHeight = 50.dp,
            navigationIcon = {
                IconButton(enabled = enableBackAction, onClick = {
                    onNavigateBack.invoke()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = ""
                    )
                }
            }
        )
    }
}

@Composable
fun BottomBar(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    val visible: Boolean by viewModel.bottomBarVisible.observeAsState(true)
    AnimatedVisibility(visible = visible) {
        NavigationBar {
            BottomNavItem.entries.forEach { item ->
                val selected =
                    navController.currentBackStackEntryAsState().value?.destination?.route == item.route
                NavigationBarItem(
                    selected = selected,
                    onClick = { navController.navigate(item.route) },
                    icon = {
                        Icon(
                            imageVector = item.selectedIcon,
                            contentDescription = stringResource(id = item.iconTextId)
                        )
                    },
                    label = {
                        Text(text = stringResource(id = item.titleTextId))
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TopBarPreview() {
    TopBar(
        title = "Departamento en Alquiler",
        visible = true,
        enableBackAction = true
    ) {

    }
}

@Preview
@Composable
fun BottomBarPreview() {
    BottomBar(MainViewModel(), navController = rememberNavController())
}