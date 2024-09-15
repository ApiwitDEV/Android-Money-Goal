package com.overshoot.moneygoal.component.scanbill.ui

import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.overshoot.moneygoal.R
import com.overshoot.moneygoal.component.scanbill.stateholder.ScanBillStateHolder
import com.overshoot.moneygoal.databinding.ScanBinding


@Composable
fun ScanContent() {
    val activityResult = LocalActivityResultRegistryOwner.current?.activityResultRegistry
    val context = LocalContext.current
    val stateHolder = remember { activityResult?.let { ScanBillStateHolder(activity = it, contentResolver = context.contentResolver) } }
    val textValue = remember { mutableStateOf("xxx") }
    Column(
        modifier = Modifier
            .systemBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val inputImage = stateHolder?.image?.value?.asImageBitmap()
        Text(text = textValue.value)
        Button(onClick = { stateHolder?.choosePhoto() }) {
            Text(text = "Choose Photo")
        }
        val lifecycleOwner = LocalLifecycleOwner.current
        inputImage?.asAndroidBitmap()?.let {
            AndroidViewBinding(
                factory = ScanBinding::inflate,
                modifier = Modifier.size(300.dp)
            ) {
                stateHolder.textElement.observe(lifecycleOwner) {
                    val textGraphic = TextGraphic(this.graphicOverlay, it)
                    this.graphicOverlay.add(textGraphic)
                }
                stateHolder.clearOverlay.observe(lifecycleOwner) {
                    this.graphicOverlay.clear()
                }
                this.imageView.setImageBitmap(it)
                this.imageView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }
        }
        inputImage?.let {
            Button(onClick = { stateHolder.runTextRecognition() }) {
                Text(text = "Scan")
            }
        }
    }
}