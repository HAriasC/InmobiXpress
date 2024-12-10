package com.inmobixpress.inmobixpress.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class District(
    val id: Int,
    val name: String,
    val location: Location,
    val province: Province
) : Parcelable
