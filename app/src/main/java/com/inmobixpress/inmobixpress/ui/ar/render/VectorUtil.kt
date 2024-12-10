package com.inmobixpress.inmobixpress.ui.ar.render

import kotlin.math.abs
import kotlin.math.sqrt

fun dot(u: FloatArray, v: FloatArray): Float {
    return ((u[X] * v[X]) + (u[Y] * v[Y]) + (u[Z] * v[Z]))
}

fun minus(u: FloatArray, v: FloatArray): FloatArray {
    return floatArrayOf(u[X] - v[X], u[Y] - v[Y], u[Z] - v[Z])
}

fun addition(u: FloatArray, v: FloatArray): FloatArray {
    return floatArrayOf(u[X] + v[X], u[Y] + v[Y], u[Z] + v[Z])
}

fun scalarProduct(r: Float, u: FloatArray): FloatArray {
    return floatArrayOf(u[X] * r, u[Y] * r, u[Z] * r)
}

fun crossProduct(u: FloatArray, v: FloatArray): FloatArray {
    return floatArrayOf(
        (u[Y] * v[Z]) - (u[Z] * v[Y]),
        (u[Z] * v[X]) - (u[X] * v[Z]),
        (u[X] * v[Y]) - (u[Y] * v[X])
    )
}

fun length(u: FloatArray): Float {
    return abs(sqrt(((u[X] * u[X]) + (u[Y] * u[Y]) + (u[Z] * u[Z])).toDouble()))
        .toFloat()
}

const val X = 0
const val Y = 1
const val Z = 2