package com.inmobixpress.inmobixpress.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.inmobixpress.inmobixpress.ui.components.ItemCard
import com.inmobixpress.inmobixpress.ui.model.PropertyItem
import com.inmobixpress.inmobixpress.ui.utils.previewListProperty

@Composable
fun HomeScreen(onNavigateToDetail: (id: Int) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PropertyList(
            propertyList = previewListProperty(), onNavigateToDetail = onNavigateToDetail
        )
    }
}

@Composable
fun PropertyList(propertyList: List<PropertyItem>, onNavigateToDetail: (id: Int) -> Unit) {
    LazyColumn {
        items(propertyList) { property ->
            ItemCard(property = property) { item ->
                onNavigateToDetail(item.id)
            }
        }
    }
}

@Preview
@Composable
fun PropertyListPreview() {
    PropertyList(
        propertyList = previewListProperty(), onNavigateToDetail = {}
    )
}