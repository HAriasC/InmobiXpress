package com.inmobixpress.inmobixpress.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Department(
    val id: Int,
    val name: String,
    val country: Country
) : Parcelable
