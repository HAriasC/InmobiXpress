package com.inmobixpress.inmobixpress.ui.model

import com.google.ar.core.Anchor
import com.inmobixpress.inmobixpress.ui.ar.render.Mesh

data class AnchorItem(
    val property: PropertyItem,
    var anchor: Anchor? = null,
    var viewMatrix: FloatArray? = null,
    var modelViewMatrix: FloatArray? = null,
    var projectionMatrix: FloatArray? = null,
    var mesh: Mesh? = null
)
