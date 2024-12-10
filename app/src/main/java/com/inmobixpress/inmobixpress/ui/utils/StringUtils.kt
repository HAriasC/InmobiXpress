package com.inmobixpress.inmobixpress.ui.utils

fun String.priceFormat(): String = "%,d".format(this.toInt())

fun String.bathroomFormat(): String {
    try {
        if (this.split(".")[1].toInt() == 0) {
            return "${this.toDouble().toInt()}"
        } else {
            return "${this.split(".")[0]} 1/2"
        }
    } catch (_: NumberFormatException) {
        return this
    }
}

fun String.formatNavRoute(): String {
    val value = this.replace("$", ".")
    return if (value.contains("?")) value.split("?")[0] else value
}