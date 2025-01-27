package com.overshoot.moneygoalapp.component.scanbill.uistatemodel

import android.graphics.Bitmap

data class ImageInfoUIState(
    val image: Bitmap? = null,
    val imageFileName: String? = null,
    val mimeType: String? = null,
    val fileSize: Long? = null
)