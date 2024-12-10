package com.inmobixpress.inmobixpress.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.inmobixpress.inmobixpress.ui.model.PropertyItem

fun Context.callProprietor(propertyItem: PropertyItem) {
    try {
        val call = Uri.parse("tel:${propertyItem.proprietor.phone}")
        this.startActivity(Intent(Intent.ACTION_DIAL, call))
    } catch (_: SecurityException) { }
}

fun Context.sendWhatsAppsProprietor(property: PropertyItem, message: String) {
    try {
        val send = Uri.parse(
            String.format(
                "https://api.whatsapp.com/send?phone=%s&text=%s",
                property.proprietor.phone,
                message
            )
        )
        this.startActivity(Intent(Intent.ACTION_VIEW, send))
    } catch (_: SecurityException) { }
}