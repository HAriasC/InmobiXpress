package com.inmobixpress.inmobixpress.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.inmobixpress.inmobixpress.data.network.implement.PropertyServiceImpl
import com.inmobixpress.inmobixpress.repository.PropertyRepository
import com.inmobixpress.inmobixpress.ui.viewmodel.MainViewModel
import com.inmobixpress.inmobixpress.ui.model.BottomNavItem
import com.inmobixpress.inmobixpress.ui.utils.formatNavRoute
import io.ktor.client.HttpClient

@ExperimentalMaterial3Api
@Composable
fun TopBar(
    title: String,
    visible: Boolean,
    enableBackAction: Boolean,
    onNavigateBack: () -> Unit,
) {
    AnimatedVisibility(visible = visible) {
        CenterAlignedTopAppBar(
            title = { Text(text = title, fontSize = 20.sp) },
            modifier = Modifier.padding(top = 4.dp),
            expandedHeight = 40.dp,
            navigationIcon = {
                IconButton(enabled = enableBackAction, onClick = {
                    onNavigateBack.invoke()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = ""
                    )
                }
            },
            windowInsets = ScaffoldDefaults.contentWindowInsets.exclude(
                TopAppBarDefaults.windowInsets.union(NavigationBarDefaults.windowInsets)
            )
        )
    }
}

@Composable
fun BottomBar(
    viewModel: MainViewModel,
    navController: NavHostController,
) {
    val visible: Boolean by viewModel.bottomBarVisible.observeAsState(true)
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        NavigationBar {
            BottomNavItem.entries.forEach { item ->
                val selected = navController.currentBackStackEntryAsState()
                    .value?.destination?.route?.formatNavRoute() == item.route
                val context = LocalContext.current
                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        if (item.name == BottomNavItem.LIVE.name) {
                            viewModel.onVisibleChanged(false)
                        }
                        if (selected.not()) {
                            navController.navigate(item.destination)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.selectedIcon,
                            contentDescription = stringResource(id = item.iconTextId)
                        )
                    },
                    label = {
                        Text(text = stringResource(id = item.titleTextId))
                    }
                )
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
    BottomBar(
        viewModel = MainViewModel(
            PropertyRepository(
                PropertyServiceImpl(
                    HttpClient()
                )
            )
        ),
        navController = rememberNavController()
    )
}