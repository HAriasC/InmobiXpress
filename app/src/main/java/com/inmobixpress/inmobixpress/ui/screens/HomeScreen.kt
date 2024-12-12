package com.inmobixpress.inmobixpress.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inmobixpress.inmobixpress.R
import com.inmobixpress.inmobixpress.data.network.implement.PropertyServiceImpl
import com.inmobixpress.inmobixpress.data.network.model.Device
import com.inmobixpress.inmobixpress.data.network.model.Image
import com.inmobixpress.inmobixpress.data.network.model.Property
import com.inmobixpress.inmobixpress.data.network.model.PropertyHasOfferType
import com.inmobixpress.inmobixpress.repository.PropertyRepository
import com.inmobixpress.inmobixpress.ui.viewmodel.MainViewModel
import com.inmobixpress.inmobixpress.ui.components.ContactBottomSheet
import com.inmobixpress.inmobixpress.ui.components.ItemCard
import com.inmobixpress.inmobixpress.ui.components.WhatsAppBottomSheet
import com.inmobixpress.inmobixpress.ui.model.Country
import com.inmobixpress.inmobixpress.ui.model.Department
import com.inmobixpress.inmobixpress.ui.model.District
import com.inmobixpress.inmobixpress.ui.model.Intermediary
import com.inmobixpress.inmobixpress.ui.model.Location
import com.inmobixpress.inmobixpress.ui.model.PropertyItem
import com.inmobixpress.inmobixpress.ui.model.PropertyType
import com.inmobixpress.inmobixpress.ui.model.Proprietor
import com.inmobixpress.inmobixpress.ui.model.Province
import com.inmobixpress.inmobixpress.ui.model.UIState
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToDetail: (id: Int) -> Unit,
    onNavigateToSearch: (id: Int) -> Unit,
) {
    viewModel.loadProperties()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PropertyList(
            viewModel = viewModel,
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToSearch = onNavigateToSearch
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyList(
    viewModel: MainViewModel,
    onNavigateToDetail: (id: Int) -> Unit,
    onNavigateToSearch: (id: Int) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val properties by viewModel.properties.collectAsState()
    val propertiesXOfferType by viewModel.propertiesXOfferType.collectAsState()
    val devices by viewModel.devices.collectAsState()
    val images by viewModel.images.collectAsState()
    var propertyItemList by remember { mutableStateOf(listOf<PropertyItem>()) }
    var messageError by rememberSaveable { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showWhatsAppBottomSheet by viewModel.whatsappBottomSheetVisible.observeAsState()
    val showContactBottomSheet by viewModel.contactBottomSheetVisible.observeAsState()
    val propertySelected = rememberSaveable { mutableIntStateOf(0) }

    Box(Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.padding(top = 85.dp)) {
            items(viewModel.foundProperties.toList(), key = { item -> item }) { (key, property) ->
                ItemCard(
                    property = property,
                    onWhatsAppClick = { item ->
                        propertySelected.intValue = item.id
                        viewModel.onWhatsAppBottomSheetVisible(true)
                    },
                    onContactClick = { item ->
                        propertySelected.intValue = item.id
                        viewModel.onContactBottomSheetVisible(true)
                        scope.launch {
                            sheetState.expand()
                        }
                    },
                    onItemClick = { item ->
                        onNavigateToDetail(item.id)
                    }
                )
            }
        }
        SearchPropertyBar(
            viewModel = viewModel,
            id = 0,
            onNavigateToSearch = onNavigateToSearch
        ) { district ->
            viewModel.foundProperties.entries.clear()
            viewModel.onSearchQueryChange(district.name)
            viewModel.foundProperties.putAll(
                propertyItemList
                    .filter { it.district.name == district.name }
                    .map { Pair("${it.id}", it) })
        }
        if (showWhatsAppBottomSheet == true) {
            WhatsAppBottomSheet(
                viewModel = viewModel,
                property = propertyItemList[propertySelected.intValue]
            )
        }
        if (showContactBottomSheet == true) {
            ContactBottomSheet(
                viewModel = viewModel,
                sheetState = sheetState,
                property = propertyItemList[propertySelected.intValue]
            )
        }

        when (properties) {
            is UIState.Loading -> {
                LaunchedEffect(key1 = properties) {
                    viewModel.onLoadingVisible(visible = true)
                }
            }

            is UIState.Success -> {
                LaunchedEffect(key1 = properties) {
                    viewModel.loadPropertiesHasOfferType()
                }
            }

            is UIState.Error -> {
                LaunchedEffect(key1 = properties) {
                    messageError = (properties as UIState.Error<List<Property>>).error.toString()
                    viewModel.onLoadingVisible(visible = false)
                    viewModel.onErrorDialogVisible(visible = true)
                }
            }

            is UIState.None -> {
                LaunchedEffect(key1 = properties) {
                    viewModel.onLoadingVisible(visible = true)
                }
            }
        }

        when (propertiesXOfferType) {
            is UIState.Loading -> {
                LaunchedEffect(key1 = propertiesXOfferType) {
                    viewModel.onLoadingVisible(visible = true)
                }
            }

            is UIState.Success -> {
                LaunchedEffect(key1 = propertiesXOfferType) {
                    viewModel.loadDevices()
                }
            }

            is UIState.Error -> {
                LaunchedEffect(key1 = propertiesXOfferType) {
                    messageError =
                        (propertiesXOfferType as UIState.Error<List<PropertyHasOfferType>>).error.toString()
                    viewModel.onLoadingVisible(visible = false)
                    viewModel.onErrorDialogVisible(visible = true)
                }
            }

            is UIState.None -> {
                LaunchedEffect(key1 = propertiesXOfferType) {
                    viewModel.onLoadingVisible(visible = true)
                }
            }
        }

        when (devices) {
            is UIState.Loading -> {
                LaunchedEffect(key1 = devices) {
                    viewModel.onLoadingVisible(visible = true)
                }
            }

            is UIState.Success -> {
                LaunchedEffect(key1 = devices) {
                    viewModel.loadImages()
                }
            }

            is UIState.Error -> {
                LaunchedEffect(key1 = devices) {
                    messageError = (devices as UIState.Error<List<Device>>).error.toString()
                    viewModel.onLoadingVisible(visible = false)
                    viewModel.onErrorDialogVisible(visible = true)
                }
            }

            is UIState.None -> {
                LaunchedEffect(key1 = devices) {
                    viewModel.onLoadingVisible(visible = true)
                }
            }
        }

        when (images) {
            is UIState.Loading -> {
                LaunchedEffect(key1 = images) {
                    viewModel.onLoadingVisible(visible = true)
                }
            }

            is UIState.Success -> {
                LaunchedEffect(key1 = images) {
                    if (properties is UIState.Success<List<Property>>
                        && devices is UIState.Success<List<Device>>
                    ) {
                        var price = 0
                        val phone = (devices as UIState.Success<List<Device>>).data[0].phone
                        val list = mutableListOf<PropertyItem>()
                        val dist = mutableListOf<District>()
                        (properties as UIState.Success<List<Property>>).data.forEach { item ->
                            if (propertiesXOfferType is UIState.Success<List<PropertyHasOfferType>>) {
                                (propertiesXOfferType as UIState.Success<List<PropertyHasOfferType>>)
                                    .data.forEach {
                                        if (it.property.id == item.id) {
                                            price = if (it.offerType.id == 0) {
                                                it.price.toInt()
                                            } else {
                                                it.price.toInt()
                                            }
                                        }
                                    }
                            }
                            val district = District(
                                id = item.district.id,
                                name = item.district.name,
                                location = Location(
                                    id = item.district.location.id,
                                    latitude = item.district.location.latitude,
                                    longitude = item.district.location.longitude,
                                    altitude = item.district.location.altitude,
                                    altitudeBase = item.district.location.altitudeBase
                                ),
                                province = Province(
                                    id = item.district.province.id,
                                    name = item.district.province.name,
                                    department = Department(
                                        id = item.district.province.department.id,
                                        name = item.district.province.department.name,
                                        country = Country(
                                            id = item.district.province.department.country.id,
                                            name = item.district.province.department.country.name,
                                            countryCode = item.district.province.department
                                                .country.countryCode
                                        )
                                    )
                                )
                            )
                            val property = PropertyItem(
                                id = item.id,
                                price = price,
                                maintenance = item.maintenance.toInt(),
                                address = item.address,
                                postalCode = item.postalCode,
                                description = item.description,
                                area = item.totalArea,
                                bedrooms = item.nBedroom,
                                bathrooms = item.nBathroom,
                                garages = item.nGarage,
                                type = when (item.propertyType.id) {
                                    1 -> PropertyType.APARTMENT
                                    2 -> PropertyType.HOUSE
                                    4 -> PropertyType.SHOP
                                    5 -> PropertyType.OFFICE
                                    else -> PropertyType.HOUSE
                                },
                                proprietor = Proprietor(
                                    id = item.user.id,
                                    name = item.user.name,
                                    lastName = item.user.lastName,
                                    motherLastName = item.user.motherLastName,
                                    phone = phone,
                                    email = item.user.email
                                ),
                                intermediary = Intermediary(
                                    id = item.user.id,
                                    name = item.user.name,
                                    lastName = item.user.lastName,
                                    motherLastName = item.user.motherLastName,
                                    phone = phone,
                                    email = item.user.email
                                ),
                                district = district,
                                location = Location(
                                    id = item.location.id,
                                    latitude = item.location.latitude,
                                    longitude = item.location.longitude,
                                    altitude = item.location.altitude,
                                    altitudeBase = item.location.altitudeBase
                                ),
                                images = (images as UIState.Success<List<Image>>).data.filter {
                                    it.property.id == item.id
                                }.map { it.url }
                            )
                            list.add(property)
                            dist.add(district)
                        }
                        propertyItemList = list
                        Log.e("LIST", propertyItemList.toString())

                        viewModel.onPropertyItemsChanged(
                            propertyItems = list.associateBy { it.id }
                        )
                        viewModel.onDistrictChanged(districts = dist.toSet().toList())
                    }
                    viewModel.onLoadingVisible(visible = false)
                }
            }

            is UIState.Error -> {
                LaunchedEffect(key1 = images) {
                    messageError =
                        (images as UIState.Error<List<Image>>).error.toString()
                    viewModel.onLoadingVisible(visible = false)
                    viewModel.onErrorDialogVisible(visible = true)
                }
            }

            is UIState.None -> {
                LaunchedEffect(key1 = images) {
                    viewModel.onLoadingVisible(visible = true)
                }
            }
        }
    }
}

@Preview
@Composable
fun PropertyListPreview() {
    PropertyList(
        viewModel = MainViewModel(
            PropertyRepository(
                PropertyServiceImpl(
                    HttpClient()
                )
            )
        ),
        onNavigateToDetail = { },
        onNavigateToSearch = { }
    )
}