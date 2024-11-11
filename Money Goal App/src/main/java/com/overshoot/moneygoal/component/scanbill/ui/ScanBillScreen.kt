package com.overshoot.moneygoal.component.scanbill.ui

import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.overshoot.moneygoal.component.scanbill.stateholder.ScanBillStateHolder
import com.overshoot.moneygoal.component.scanbill.stateholder.ScanViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel


@Composable
fun ScanBillScreen() {
    val viewModel = koinViewModel<ScanViewModel>()
    val activityResult = LocalActivityResultRegistryOwner.current?.activityResultRegistry
    val context = LocalContext.current
    val density = LocalDensity.current
    val stateHolder = remember { activityResult?.let { ScanBillStateHolder(activity = it, contentResolver = context.contentResolver) } }
    val inputImage = stateHolder?.image?.collectAsStateWithLifecycle()
    var imageHeight by remember { mutableStateOf(0.dp) }
    LaunchedEffect(Unit) {
        stateHolder?.image?.collectLatest {
            viewModel.collectImage(it)
        }
    }
    LaunchedEffect(Unit) {
        stateHolder?.loadCachePhoto(viewModel.loadCollectedImage())
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
                    inputImage?.value?.asImageBitmap()?.let { image ->
                        Image(
                            modifier = Modifier.height(imageHeight),
                            bitmap = image,
                            contentDescription = ""
                        )
                    }
                }
            }
            item {
                stateHolder?.text?.collectAsStateWithLifecycle().let { t ->
                    Text(text = t?.value?:"")
                }
            }
        }
    }
}