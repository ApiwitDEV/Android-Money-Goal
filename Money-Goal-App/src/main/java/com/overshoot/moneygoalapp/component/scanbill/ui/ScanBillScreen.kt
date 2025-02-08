package com.overshoot.moneygoalapp.component.scanbill.ui

import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.overshoot.moneygoalapp.R
import com.overshoot.moneygoalapp.component.scanbill.stateholder.ScanBillStateHolder
import com.overshoot.moneygoalapp.component.scanbill.stateholder.ScanBillStatus
import com.overshoot.moneygoalapp.component.scanbill.stateholder.ScanBillUIState
import com.overshoot.moneygoalapp.component.scanbill.stateholder.ScanBillViewModel
import com.overshoot.moneygoalapp.theme.MoneyGoalTheme
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.round

@Composable
fun ScanBillScreen(scanBillViewModel: ScanBillViewModel) {
    val activityResult = LocalActivityResultRegistryOwner.current?.activityResultRegistry
    val context = LocalContext.current
    val stateHolder = remember { activityResult?.let { ScanBillStateHolder(activity = it, contentResolver = context.contentResolver) } }
    val uiState = scanBillViewModel.uiState.collectAsStateWithLifecycle().value
    LaunchedEffect(Unit) {
        stateHolder?.image?.collectLatest {
            if (scanBillViewModel._imageInfo == null || scanBillViewModel._imageInfo != stateHolder.image.value) {
                scanBillViewModel.collectImage(it)
            }
        }
    }
    LaunchedEffect(Unit) {
        scanBillViewModel.loadCollectedImage()?.also { image ->
            stateHolder?.loadCachePhoto(image)
        }
    }
    ScanBillContent(
        stateHolder = stateHolder,
        uiState = uiState,
        onSubmit = {
            scanBillViewModel.submitImage()
        }
    )
}

@Composable
fun ScanBillContent(
    stateHolder: ScanBillStateHolder?,
    uiState: ScanBillUIState,
    onSubmit: () -> Unit
) {
    val inputImage = stateHolder?.image?.collectAsStateWithLifecycle()
    var imageHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        floatingActionButton = {
//            Button(onClick = { stateHolder?.choosePhoto() }) {
//                Text(text = "Choose Photo")
//            }
            FloatingActionButton(
                onClick = { stateHolder?.choosePhoto() },
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 2.dp)
            ) {
                Image(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(32.dp),
                    painter = painterResource(R.drawable.baseline_image_24),
                    contentDescription = null
                )
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
                    inputImage?.value?.imageUri?.let { imageUri ->
                        AsyncImage(
                            model = imageUri,
                            contentDescription = null,
                        )
                    }?: let {
                        inputImage?.value?.image?.asImageBitmap()?.let { image ->
                            Image(
                                modifier = Modifier.height(imageHeight),
                                bitmap = image,
                                contentDescription = ""
                            )
                        }
                    }
                }
            }
            item {
                Spacer(Modifier.height(16.dp))
                when(uiState.status) {
                    ScanBillStatus.PREPARING, ScanBillStatus.LOADING, ScanBillStatus.ANALYZING -> {
                        ProgressBar(uiState.progress, uiState.status)
                    }
                    ScanBillStatus.DONE, ScanBillStatus.ERROR -> {
                        Text(text = uiState.detail)
                        Button(
                            enabled = uiState.isSubmitAble,
                            onClick = onSubmit
                        ) {
                            Text(
                                text = "Analyze"
                            )
                        }
                    }
                }
                Spacer(Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun ProgressBar(progress: Double, scanBillStatus: ScanBillStatus) {
    Box(contentAlignment = Alignment.Center) {
        if (scanBillStatus == ScanBillStatus.PREPARING || scanBillStatus == ScanBillStatus.ANALYZING) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(120.dp),
                strokeWidth = 8.dp
            )
        }
        else {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(120.dp),
                strokeWidth = 8.dp,
                progress = {
                    (progress/100).toFloat()
                }
            )
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = when(scanBillStatus) {
                ScanBillStatus.PREPARING -> "Preparing"
                ScanBillStatus.LOADING -> (round(progress*100)/100).toString()
                ScanBillStatus.ANALYZING -> "Analyzing"
                else -> ""
            },
            fontSize = 20.sp
        )
    }
}

@Composable
@Preview
fun ScanBillPreview() {
    val context = LocalContext.current
    val activityResult = LocalActivityResultRegistryOwner.current?.activityResultRegistry
    val stateHolder = remember { activityResult?.let { ScanBillStateHolder(activity = it, contentResolver = context.contentResolver) } }
    ScanBillContent(
        stateHolder = stateHolder,
        uiState = ScanBillUIState(
            status = ScanBillStatus.LOADING,
            isSubmitAble = true,
            progress = 55.538,
            detail = "aaa"
        ),
        onSubmit = {}
    )
}

@Composable
@Preview(showBackground = true)
fun ProgressBarDialogPreview() {
    MoneyGoalTheme {
        ProgressBar(progress = 40.452, scanBillStatus = ScanBillStatus.ANALYZING)
    }
}