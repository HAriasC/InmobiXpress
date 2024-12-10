package com.inmobixpress.inmobixpress.ui.model

import com.google.android.libraries.places.api.model.Place

data class ServiceMarker(
    val place:Place,
    val type: FilterType
)
