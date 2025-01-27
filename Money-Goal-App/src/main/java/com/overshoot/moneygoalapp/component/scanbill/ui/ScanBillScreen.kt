package com.overshoot.moneygoalapp.component.scanbill.ui

import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.overshoot.moneygoalapp.common.ui.LoadingDialog
import com.overshoot.moneygoalapp.component.scanbill.stateholder.ScanBillStateHolder
import com.overshoot.moneygoalapp.component.scanbill.stateholder.ScanBillViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ScanBillScreen(scanBillViewModel: ScanBillViewModel) {
    val activityResult = LocalActivityResultRegistryOwner.current?.activityResultRegistry
    val context = LocalContext.current
    val density = LocalDensity.current
    val stateHolder = remember { activityResult?.let { ScanBillStateHolder(activity = it, contentResolver = context.contentResolver) } }
    val inputImage = stateHolder?.image?.collectAsStateWithLifecycle()
    var imageHeight by remember { mutableStateOf(0.dp) }
    val uiState = scanBillViewModel.uiState.collectAsStateWithLifecycle().value
    LaunchedEffect(Unit) {
        stateHolder?.image?.collectLatest {
            scanBillViewModel.collectImage(it)
        }
    }
    LaunchedEffect(Unit) {
        scanBillViewModel.loadCollectedImage()?.also { image ->
            stateHolder?.loadCachePhoto(image)
        }
    }
    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        floatingActionButton = {
            FloatingActionButton(onClick = { stateHolder?.choosePhoto() }) {
                Text(text = "Choose Photo")
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
                .fillMaxSize()
                .onSizeChanged {
                    with(density) {
                        imageHeight = it.width.toDp()
                    }
                }
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                if (imageHeight != 0.dp) {
                    inputImage?.value?.image?.asImageBitmap()?.let { image ->
                        Image(
                            modifier = Modifier.height(imageHeight),
                            bitmap = image,
                            contentDescription = ""
                        )
                    }
                }
            }
//            item {
//                stateHolder?.text?.collectAsStateWithLifecycle().let { t ->
//                    Text(text = t?.value?:"")
//                }
//            }
            item {
                Text(text = uiState.detail)
            }
            item {
                Button(
                    enabled = uiState.isSubmitAble,
                    onClick = {
                        scanBillViewModel.submitImage()
                    }
                ) {
                    Text("Submit")
                }
            }
        }
    }
    if (uiState.isLoading) {
        LoadingDialog()
    }
}