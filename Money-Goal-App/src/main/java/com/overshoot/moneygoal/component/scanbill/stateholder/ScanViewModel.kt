package com.overshoot.moneygoal.component.scanbill.stateholder

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel

class ScanViewModel: ViewModel() {

    private var _image: Bitmap? = null

    fun collectImage(image: Bitmap?) {
        _image = image
    }

    fun loadCollectedImage(): Bitmap? {
        return _image
    }

}