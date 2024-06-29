package com.inmobixpress.inmobixpress.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inmobixpress.inmobixpress.ui.model.TabItem
import com.inmobixpress.inmobixpress.ui.utils.previewDetailTabList
import com.inmobixpress.inmobixpress.ui.utils.previewListProperty

@Composable
fun SectionTabs(tabs: List<TabItem>) {
    var tabIndex by remember { mutableIntStateOf(0) }
    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = tabIndex
        ) {
            tabs.forEachIndexed { index, item ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    modifier = Modifier.height(60.dp),
                    text = { Text(text = item.title) },
                    icon = { Icon(imageVector = item.icon, contentDescription = "") }
                )
            }
        }
        tabs.getOrNull(tabIndex)?.screen?.invoke()
    }
}

@Preview
@Composable
fun SectionTabsPreview() {
    SectionTabs(previewDetailTabList(previewListProperty()[0]))
}