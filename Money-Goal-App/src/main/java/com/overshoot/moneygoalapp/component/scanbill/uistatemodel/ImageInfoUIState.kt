package com.overshoot.moneygoalapp.component.scanbill.uistatemodel

import android.graphics.Bitmap
import android.net.Uri

data class ImageInfoUIState(
    val image: Bitmap? = null,
    val imageFileName: String? = null,
    val mimeType: String? = null,
    val fileSize: Long? = null,
    val imageUri: Uri? = null
)