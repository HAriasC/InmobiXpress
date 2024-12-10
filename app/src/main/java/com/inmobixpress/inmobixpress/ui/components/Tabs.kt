package com.inmobixpress.inmobixpress.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.inmobixpress.inmobixpress.data.network.implement.PropertyServiceImpl
import com.inmobixpress.inmobixpress.repository.PropertyRepository
import com.inmobixpress.inmobixpress.ui.viewmodel.MainViewModel
import com.inmobixpress.inmobixpress.ui.model.TabItem
import com.inmobixpress.inmobixpress.ui.utils.previewContactTabList
import com.inmobixpress.inmobixpress.ui.utils.previewDetailTabList
import com.inmobixpress.inmobixpress.ui.utils.previewListProperty
import io.ktor.client.HttpClient

@Composable
fun SectionTabs(viewModel: MainViewModel, tabs: List<TabItem>) {
    var tabIndex by remember { mutableIntStateOf(0) }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        TabRow(
            selectedTabIndex = tabIndex,
            modifier = Modifier.width(300.dp)
        ) {
            tabs.forEachIndexed { index, item ->
                Tab(
                    selected = tabIndex == index,
                    onClick = {
                        if (tabIndex != index) {
                            viewModel.serviceMarkers.clear()
                        }
                        tabIndex = index
                    },
                    text = { Text(text = item.title) },
                    icon = { Icon(imageVector = item.icon, contentDescription = "") }
                )
            }
        }
        tabs.getOrNull(tabIndex)?.screen?.invoke()
    }
}

@Composable
fun ContactTabs(tabs: List<TabItem>) {
    var tabIndex by remember { mutableIntStateOf(0) }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        TabRow(
            selectedTabIndex = tabIndex
        ) {
            tabs.forEachIndexed { index, item ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = ""
                            )
                            Text(
                                text = item.title,
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }
                    }
                )
            }
        }
        tabs.getOrNull(tabIndex)?.screen?.invoke()
    }
}

@Preview
@Composable
fun SectionTabsPreview() {
    CustomTab(
        viewModel = MainViewModel(
            PropertyRepository(
                PropertyServiceImpl(
                    HttpClient()
                )
            )
        ),
        items = previewDetailTabList(
            viewModel = MainViewModel(
                PropertyRepository(
                    PropertyServiceImpl(
                        HttpClient()
                    )
                )
            ),
            property = previewListProperty()[0]
        ),
        modifier = Modifier.padding(vertical = 10.dp)
    ) {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ContactTabsPreview() {
    CustomTab(
        viewModel = MainViewModel(
            PropertyRepository(
                PropertyServiceImpl(
                    HttpClient()
                )
            )
        ),
        items = previewContactTabList(
            viewModel = MainViewModel(
                PropertyRepository(
                    PropertyServiceImpl(
                        HttpClient()
                    )
                )
            ),
            sheetState = rememberModalBottomSheetState(),
            property = previewListProperty()[0]
        ),
        modifier = Modifier.padding(top = 10.dp)
    ) {

    }
}

@Preview
@Composable
fun SegmentedButtonMultiSelectSample() {
    val checkedList = remember { mutableStateListOf<Int>() }
    val options = listOf("Favorites", "Trending", "Saved")
    val icons =
        listOf(
            Icons.Filled.StarBorder,
            Icons.AutoMirrored.Filled.TrendingUp,
            Icons.Filled.BookmarkBorder
        )
    MultiChoiceSegmentedButtonRow {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                icon = {
                    SegmentedButtonDefaults.Icon(active = index in checkedList) {
                        Icon(
                            imageVector = icons[index],
                            contentDescription = null,
                            modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                        )
                    }
                },
                onCheckedChange = {
                    if (index in checkedList) {
                        checkedList.remove(index)
                    } else {
                        checkedList.add(index)
                    }
                },
                checked = index in checkedList
            ) {
                Text(label)
            }
        }
    }
}

@Composable
fun CustomTabIndicator(
    indicatorWidth: Dp,
    indicatorOffset: Dp,
    indicatorColor: Color,
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(
                width = indicatorWidth,
            )
            .offset(
                x = indicatorOffset,
            )
            .clip(
                shape = CircleShape,
            )
            .background(
                color = indicatorColor,
            ),
    )
}

@Composable
fun CustomTabItem(
    isSelected: Boolean,
    onClick: () -> Unit,
    tabWidth: Dp,
    icon: ImageVector,
    text: String,
) {
    val tabTextColor: Color by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.surfaceDim
        } else {
            MaterialTheme.colorScheme.surfaceTint
        },
        animationSpec = tween(easing = LinearEasing),
    )
    Column(
        modifier = Modifier.clickable { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .width(tabWidth)
                .weight(1.0f)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "",
                modifier = Modifier
                    .width(18.dp),
                tint = tabTextColor
            )
            Text(
                text = text,
                modifier = Modifier
                    .clip(CircleShape)
                    .padding(start = 4.dp),
                color = tabTextColor,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Left,
            )
        }
    }
}

@Composable
fun CustomTab(
    viewModel: MainViewModel,
    items: List<TabItem>,
    modifier: Modifier = Modifier,
    tabWidth: Dp = 120.dp,
    onItemClick: () -> Unit = { },
) {
    val (selected, setSelected) = remember {
        mutableIntStateOf(0)
    }
    val indicatorOffset: Dp by animateDpAsState(
        targetValue = tabWidth * selected,
        animationSpec = tween(easing = LinearEasing),
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .height(intrinsicSize = IntrinsicSize.Min),
            tonalElevation = 10.dp,
            shadowElevation = 10.dp

        ) {
            CustomTabIndicator(
                indicatorWidth = tabWidth,
                indicatorOffset = indicatorOffset,
                indicatorColor = MaterialTheme.colorScheme.primary,
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .horizontalScroll(rememberScrollState()),
            ) {
                items.mapIndexed { index, item ->
                    val isSelected = index == selected
                    viewModel.onTabIndexChanged(index = if (isSelected) index else 0)
                    CustomTabItem(
                        isSelected = isSelected,
                        onClick = {
                            setSelected(index)
                            if (isSelected.not()) {
                                onItemClick()
                            }
                        },
                        tabWidth = tabWidth,
                        icon = item.icon,
                        text = item.title,
                    )
                }
            }
        }
        items.getOrNull(selected)?.screen?.invoke()
    }
}