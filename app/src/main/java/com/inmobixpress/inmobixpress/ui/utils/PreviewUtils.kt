package com.inmobixpress.inmobixpress.ui.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsBike
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.inmobixpress.inmobixpress.ui.viewmodel.MainViewModel
import com.inmobixpress.inmobixpress.ui.model.Country
import com.inmobixpress.inmobixpress.ui.model.Department
import com.inmobixpress.inmobixpress.ui.model.District
import com.inmobixpress.inmobixpress.ui.model.Intermediary
import com.inmobixpress.inmobixpress.ui.model.Location
import com.inmobixpress.inmobixpress.ui.model.PropertyItem
import com.inmobixpress.inmobixpress.ui.model.PropertyType
import com.inmobixpress.inmobixpress.ui.model.Proprietor
import com.inmobixpress.inmobixpress.ui.model.Province
import com.inmobixpress.inmobixpress.ui.model.TabItem
import com.inmobixpress.inmobixpress.ui.screens.ContactScreen
import com.inmobixpress.inmobixpress.ui.screens.DetailScreen
import com.inmobixpress.inmobixpress.ui.screens.NeighborhoodScreen
import com.inmobixpress.inmobixpress.ui.screens.ServiceScreen
import com.inmobixpress.inmobixpress.ui.screens.VisitScreen
import kotlinx.coroutines.flow.flowOf

fun previewListProperty() = listOf(
    PropertyItem(
        id = 0,
        price = 7000,
        maintenance = 600,
        address = "Calle Roma 314",
        postalCode = "",
        description = "Se alquila moderno departamento en zona exlusiva.",
        area = 200.0,
        bedrooms = 3,
        bathrooms = 3.0,
        garages = 2,
        type = PropertyType.APARTMENT,
        proprietor = Proprietor(
            id = 0,
            name = "",
            lastName = "",
            motherLastName = "",
            phone = "+51983726582",
            email = "h.arias.c3@gmail.com"
        ),
        intermediary = Intermediary(
            id = 0,
            name = "",
            lastName = "",
            motherLastName = "",
            phone = "+51983726582",
            email = ""
        ),
        district = District(
            id = 0,
            name = "Miraflores",
            location = Location(
                id = 0,
                latitude = -12.1218773,
                longitude = -77.03053899999999,
                altitude = 0.0,
                altitudeBase = 0.0
            ),
            province = Province(
                id = 0,
                name = "Lima",
                department = Department(
                    id = 0,
                    name = "Lima",
                    country = Country(id = 0, name = "", countryCode = "51")
                )
            )
        ),
        location = Location(
            id = 0,
            latitude = -12.123554,
            longitude = -77.037894,
            altitude = 1.0,
            altitudeBase = 0.0
        ),
        images = emptyList()
    ), PropertyItem(
        id = 1,
        price = 2900,
        maintenance = 400,
        address = "Av Reducto",
        postalCode = "",
        description = "Se alquila hermoso dúplex con apmplia terraza.",
        area = 122.0,
        bedrooms = 1,
        bathrooms = 1.0,
        garages = 2,
        type = PropertyType.APARTMENT,
        proprietor = Proprietor(
            id = 0,
            name = "",
            lastName = "",
            motherLastName = "",
            phone = "+51942160648",
            email = ""
        ),
        Intermediary(
            id = 0,
            name = "",
            lastName = "",
            motherLastName = "",
            phone = "+51942160648",
            email = ""
        ),
        district = District(
            id = 0,
            name = "Miraflores",
            location = Location(
                id = 0,
                latitude = -12.1218773,
                longitude = -77.03053899999999,
                altitude = 0.0,
                altitudeBase = 0.0
            ),
            province = Province(
                id = 0,
                name = "Lima",
                department = Department(
                    id = 0,
                    name = "Lima",
                    country = Country(id = 0, name = "", countryCode = "51")
                )
            )
        ),
        location = Location(
            id = 0,
            latitude = -12.131138,
            longitude = -77.023373,
            altitude = 1.0,
            altitudeBase = 0.0
        ),
        images = emptyList()
    ), PropertyItem(
        id = 2,
        price = 7000,
        maintenance = 600,
        address = "Calle Roma 314",
        postalCode = "",
        description = "Se alquila moderno departamento en zona exlusiva.",
        area = 200.0,
        bedrooms = 3,
        bathrooms = 3.0,
        garages = 2,
        type = PropertyType.APARTMENT,
        proprietor = Proprietor(
            id = 0,
            name = "",
            lastName = "",
            motherLastName = "",
            phone = "+51983726582",
            email = "h.arias.c3@gmail.com"
        ),
        intermediary = Intermediary(
            id = 0,
            name = "",
            lastName = "",
            motherLastName = "",
            phone = "+51983726582",
            email = ""
        ),
        district = District(
            id = 0,
            name = "Miraflores",
            location = Location(
                id = 0,
                latitude = -12.1218773,
                longitude = -77.03053899999999,
                altitude = 0.0,
                altitudeBase = 0.0
            ),
            province = Province(
                id = 0,
                name = "Lima",
                department = Department(
                    id = 0,
                    name = "Lima",
                    country = Country(id = 0, name = "", countryCode = "51")
                )
            )
        ),
        location = Location(
            id = 0,
            latitude = -12.121710717290028,
            longitude = -77.037089662254,
            altitude = 1.0,
            altitudeBase = 0.0
        ),
        images = emptyList()
    ), PropertyItem(
        id = 3,
        price = 2900,
        maintenance = 400,
        address = "Av Reducto",
        postalCode = "",
        description = "Se alquila hermoso dúplex con apmplia terraza.",
        area = 122.0,
        bedrooms = 1,
        bathrooms = 1.0,
        garages = 2,
        type = PropertyType.APARTMENT,
        proprietor = Proprietor(
            id = 0,
            name = "",
            lastName = "",
            motherLastName = "",
            phone = "+51942160648",
            email = ""
        ),
        Intermediary(
            id = 0,
            name = "",
            lastName = "",
            motherLastName = "",
            phone = "+51942160648",
            email = ""
        ),
        district = District(
            id = 0,
            name = "Miraflores",
            location = Location(
                id = 0,
                latitude = -12.1218773,
                longitude = -77.03053899999999,
                altitude = 0.0,
                altitudeBase = 0.0
            ),
            province = Province(
                id = 0,
                name = "Lima",
                department = Department(
                    id = 0,
                    name = "Lima",
                    country = Country(id = 0, name = "", countryCode = "51")
                )
            )
        ),
        location = Location(
            id = 0,
            latitude = -12.121323926428023,
            longitude = -77.03705539671716,
            altitude = 1.0,
            altitudeBase = 0.0
        ),
        images = emptyList()
    ), PropertyItem(
        id = 4,
        price = 7000,
        maintenance = 600,
        address = "Calle Francia 666",
        postalCode = "",
        description = "Se alquila moderno departamento en zona exlusiva.",
        area = 200.0,
        bedrooms = 3,
        bathrooms = 3.0,
        garages = 2,
        type = PropertyType.APARTMENT,
        proprietor = Proprietor(
            id = 0,
            name = "",
            lastName = "",
            motherLastName = "",
            phone = "+51983726582",
            email = "h.arias.c3@gmail.com"
        ),
        intermediary = Intermediary(
            id = 0,
            name = "",
            lastName = "",
            motherLastName = "",
            phone = "+51983726582",
            email = ""
        ),
        district = District(
            id = 0,
            name = "Miraflores",
            location = Location(
                id = 0,
                latitude = -12.1218773,
                longitude = -77.03053899999999,
                altitude = 0.0,
                altitudeBase = 0.0
            ),
            province = Province(
                id = 0,
                name = "Lima",
                department = Department(
                    id = 0,
                    name = "Lima",
                    country = Country(id = 0, name = "", countryCode = "51")
                )
            )
        ),
        location = Location(
            id = 0,
            latitude = -12.122490930889917,
            longitude = -77.03741972519838,
            altitude = 1.0,
            altitudeBase = 0.0
        ),
        images = emptyList()
    ), PropertyItem(
        id = 4,
        price = 2000,
        maintenance = 200,
        address = "Calle Bartolome Herrera 133",
        postalCode = "",
        description = "Se alquila moderno departamento en zona comercial.",
        area = 200.0,
        bedrooms = 3,
        bathrooms = 3.0,
        garages = 2,
        type = PropertyType.APARTMENT,
        proprietor = Proprietor(
            id = 0,
            name = "",
            lastName = "",
            motherLastName = "",
            phone = "+51983726582",
            email = "h.arias.c3@gmail.com"
        ),
        intermediary = Intermediary(
            id = 0,
            name = "",
            lastName = "",
            motherLastName = "",
            phone = "+51983726582",
            email = ""
        ),
        district = District(
            id = 0,
            name = "Lince",
            location = Location(
                id = 0,
                latitude = -12.1218773,
                longitude = -77.03053899999999,
                altitude = 0.0,
                altitudeBase = 0.0
            ),
            province = Province(
                id = 0,
                name = "Lima",
                department = Department(
                    id = 0,
                    name = "Lima",
                    country = Country(id = 0, name = "", countryCode = "51")
                )
            )
        ),
        location = Location(
            id = 0,
            latitude = -12.08122514639542,
            longitude = -77.03567200443825,
            altitude = 1.0,
            altitudeBase = 0.0
        ),
        images = emptyList()
    )
)

fun previewDetailTabList(viewModel: MainViewModel, property: PropertyItem) = listOf(
    TabItem(title = "Detalle", icon = property.type.icon) {
        DetailScreen(
            viewModel = viewModel,
            property = property
        )
    },
    TabItem(title = "Vecindario", icon = Icons.AutoMirrored.Outlined.DirectionsBike) {
        NeighborhoodScreen(
            viewModel = viewModel,
            property = property
        )
    },
    TabItem(title = "Servicios", icon = Icons.Outlined.Construction) {
        ServiceScreen(
            viewModel = viewModel,
            property = property
        )
    }
)

@OptIn(ExperimentalMaterial3Api::class)
fun previewContactTabList(
    viewModel: MainViewModel,
    sheetState: SheetState,
    property: PropertyItem,
) = listOf(
    TabItem(title = "Contactar", icon = Icons.Outlined.Mail) {
        ContactScreen(
            viewModel = viewModel,
            sheetState = sheetState,
            property = property
        )
    },
    TabItem(title = "Visitar", icon = Icons.Outlined.Today) {
        VisitScreen(
            viewModel = viewModel,
            sheetState = sheetState,
            property = property
        )
    }
)

fun previewDistricts() = flowOf(
    listOf(
        District(
            id = 0,
            name = "Barranco",
            location = Location(
                id = 0,
                latitude = -12.143726599999999,
                longitude = -77.0190228,
                altitude = 0.0,
                altitudeBase = 0.0
            ),
            province = Province(
                id = 0,
                name = "Lima",
                department = Department(
                    id = 0,
                    name = "Lima",
                    country = Country(id = 0, name = "", countryCode = "51")
                )
            )
        ),
        District(
            id = 1,
            name = "Lince",
            location = Location(
                id = 0,
                latitude = -12.084807500000002,
                longitude = -77.0355629,
                altitude = 0.0,
                altitudeBase = 0.0
            ),
            province = Province(
                id = 0,
                name = "Lima",
                department = Department(
                    id = 0,
                    name = "Lima",
                    country = Country(id = 0, name = "", countryCode = "51")
                )
            )
        ),
        District(
            id = 2,
            name = "Miraflores",
            location = Location(
                id = 0,
                latitude = -12.1218773,
                longitude = -77.03053899999999,
                altitude = 0.0,
                altitudeBase = 0.0
            ),
            province = Province(
                id = 0,
                name = "Lima",
                department = Department(
                    id = 0,
                    name = "Lima",
                    country = Country(id = 0, name = "", countryCode = "51")
                )
            )
        ),
        District(
            id = 3,
            name = "San Borja",
            location = Location(
                id = 0,
                latitude = -12.1005158,
                longitude = -76.99310740000001,
                altitude = 0.0,
                altitudeBase = 0.0
            ),
            province = Province(
                id = 0,
                name = "Lima",
                department = Department(
                    id = 0,
                    name = "Lima",
                    country = Country(id = 0, name = "", countryCode = "51")
                )
            )
        ),
        District(
            id = 4,
            name = "San Isidro",
            location = Location(
                id = 0,
                latitude = -12.0970297,
                longitude = -77.03391189999999,
                altitude = 0.0,
                altitudeBase = 0.0
            ),
            province = Province(
                id = 0,
                name = "Lima",
                department = Department(
                    id = 0,
                    name = "Lima",
                    country = Country(id = 0, name = "", countryCode = "51")
                )
            )
        ),
        District(
            id = 5,
            name = "Santiago de Surco",
            location = Location(
                id = 0,
                latitude = -12.122978,
                longitude = -76.9856739,
                altitude = 0.0,
                altitudeBase = 0.0
            ),
            province = Province(
                id = 0,
                name = "Lima",
                department = Department(
                    id = 0,
                    name = "Lima",
                    country = Country(id = 0, name = "", countryCode = "51")
                )
            )
        )
    )
)

fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int,
): BitmapDescriptor? {
    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}
