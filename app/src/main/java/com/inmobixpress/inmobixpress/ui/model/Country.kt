package com.inmobixpress.inmobixpress.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    val id: Int,
    val name: String,
    val countryCode: String
) : Parcelable
