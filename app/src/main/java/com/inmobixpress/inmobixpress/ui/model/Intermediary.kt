package com.inmobixpress.inmobixpress.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Intermediary(
    val id: Int,
    val name: String,
    val lastName: String,
    val motherLastName: String,
    val phone : String,
    val email: String
) : Parcelable
