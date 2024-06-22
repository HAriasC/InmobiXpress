package com.inmobixpress.inmobixpress.ui.utils

import com.inmobixpress.inmobixpress.R
import com.inmobixpress.inmobixpress.ui.model.Country
import com.inmobixpress.inmobixpress.ui.model.Department
import com.inmobixpress.inmobixpress.ui.model.District
import com.inmobixpress.inmobixpress.ui.model.Location
import com.inmobixpress.inmobixpress.ui.model.PropertyItem
import com.inmobixpress.inmobixpress.ui.model.PropertyType
import com.inmobixpress.inmobixpress.ui.model.Proprietor
import com.inmobixpress.inmobixpress.ui.model.Province

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
            phone = 999,
            email = ""
        ),
        district = District(
            id = 0,
            name = "Miraflores",
            Province(
                id = 0,
                name = "Lima",
                department = Department(
                    id = 0,
                    name = "Lima",
                    country = Country(id = 0, name = "", countryCode = 51)
                )
            )
        ),
        location = Location(id = 0, latitude = -12.123554, longitude = -77.037894, altitude = 1.0),
        images = listOf(R.drawable.image1, R.drawable.image2)
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
            phone = 999,
            email = ""
        ),
        district = District(
            id = 0,
            name = "Miraflores",
            Province(
                id = 0,
                name = "Lima",
                department = Department(
                    id = 0,
                    name = "Lima",
                    country = Country(id = 0, name = "", countryCode = 51)
                )
            )
        ),
        location = Location(id = 0, latitude = -12.131138, longitude = -77.023373, altitude = 1.0),
        images = listOf(R.drawable.image2, R.drawable.image1)
    )
)