package com.overshoot.moneygoalapp.component.scanbill.stateholder

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.FileDescriptor
import java.io.IOException

class ScanBillStateHolder(
    private val activity: ActivityResultRegistry,
    private val contentResolver: ContentResolver
) {

    private val _image = MutableStateFlow<Bitmap?>(null)
    val image: StateFlow<Bitmap?> = _image

    private val _imageDimension = mutableStateOf<Pair<Int, Int>?>(null)
    val imageDimension: State<Pair<Int, Int>?> = _imageDimension

    private val _textElement = MutableLiveData<Text.Element>()
    val textElement: LiveData<Text.Element> = _textElement

    private val _text = MutableStateFlow("")
    val text: StateFlow<String> = _text

    private val _clearOverlay = MutableLiveData<Unit>()
    val clearOverlay: LiveData<Unit> = _clearOverlay

    fun choosePhoto() {
        val pickMedia = activity.register("", ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                _image.value = getBitmapFromUri(uri)
                runTextRecognition()
            } else {
                Log.d("PhotoPicker", "No media selected")
                _image.value = Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888)
            }
        }

        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    fun loadCachePhoto(image: Bitmap?) {
        _image.value = image
        runTextRecognition()
    }

    fun setImageDimension(width: Int, height: Int) {
        _imageDimension.value = Pair(width, height)
    }

    @Throws(IOException::class)
    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val parcelFileDescriptor: ParcelFileDescriptor? = contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor: FileDescriptor? = parcelFileDescriptor?.fileDescriptor
        val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor?.close()
        return image
    }

    private fun runTextRecognition() {
        val  inputImage: Bitmap? = _image.value
        val image = inputImage?.let { InputImage.fromBitmap(it, 0) }
        val recognizer = TextRecognition.getClient(
            TextRecognizerOptions.DEFAULT_OPTIONS
        )
        image?.run {
            recognizer.process(this)
                .addOnSuccessListener { texts ->
                    processTextRecognitionResult(texts)
                }
                .addOnFailureListener { e -> // Task failed with an exception
                    e.printStackTrace()
                }
        }
    }

    private fun processTextRecognitionResult(texts: Text) {
        _clearOverlay.value = Unit
        val blocks: List<Text.TextBlock> = texts.textBlocks
        if (blocks.isEmpty()) {
            _text.value = "No text found"
        }
        blocks.forEach { block ->
            block.lines.forEach { line ->
                line.elements.forEach { element ->
                    _text.value = _text.value.plus(element.text)
                }
            }
        }
    }

    private fun processTextRecognitionResultExample(texts: Text) {
        _clearOverlay.value = Unit
        val blocks: List<Text.TextBlock> = texts.textBlocks
        if (blocks.isEmpty()) {
//        showToast("No text found")
        }
        for (i in blocks.indices) {
            val lines: List<Text.Line> = blocks[i].lines
            for (j in lines.indices) {
                val elements: List<Text.Element> = lines[j].elements
                for (k in elements.indices) {
//                    delay(50)
                    _textElement.value = elements[k]
//                val textGraphic: Graphic = TextGraphic(mGraphicOverlay, elements[k])
//                mGraphicOverlay.add(textGraphic)
                }
            }
        }
    }
}