package com.overshoot.moneygoalapp.component.scanbill.stateholder

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overshoot.data.repository.BillReceiptRepository
import com.overshoot.moneygoalapp.component.scanbill.uistatemodel.ImageInfoUIState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class ScanBillStatus {
    PREPARING,
    LOADING,
    ANALYZING,
    ERROR,
    CANCELING,
    DONE
}

data class ScanBillUIState(
    val status: ScanBillStatus,
    val isSubmitAble: Boolean,
    val progress: Double,
    val detail: String
)

class ScanBillViewModel(private val billReceiptRepository: BillReceiptRepository): ViewModel() {

    private var uploadJob: Job? = null

    private val _uiState = MutableStateFlow(ScanBillUIState(status = ScanBillStatus.DONE, isSubmitAble = false, progress = 0.0, detail = ""))
    val uiState = _uiState.asStateFlow()

    var imageInfo: ImageInfoUIState? = null

    fun collectImage(image: ImageInfoUIState?) {
        imageInfo = image
        _uiState.value = ScanBillUIState(
            status = ScanBillStatus.DONE,
            isSubmitAble = true,
            progress = 0.0,
            detail = ""
        )
    }

    fun loadCollectedImage(): ImageInfoUIState? {
        return imageInfo
    }

    fun submitImage() {
        if (imageInfo?.imageUri != null && imageInfo?.imageFileName != null && imageInfo?.mimeType != null) {
            uploadJob = Job()
            viewModelScope.launch(uploadJob!!) {
                billReceiptRepository.chunkSubmitBillReceipt(
                    image = imageInfo?.imageUri!!,
                    filename = imageInfo?.imageFileName!!,
                    type = imageInfo?.mimeType!!
                )
                    .collect { result ->
                        result
                            .onSuccess {
                                when(it.status) {
                                    "preparing" -> {
                                        _uiState.value = ScanBillUIState(
                                            status = ScanBillStatus.PREPARING,
                                            isSubmitAble = false,
                                            progress = 0.0,
                                            detail = ""
                                        )
                                    }
                                    "incomplete" -> {
                                        _uiState.value = ScanBillUIState(
                                            status = ScanBillStatus.LOADING,
                                            isSubmitAble = false,
                                            progress = it.progress,
                                            detail = ""
                                        )
                                    }
                                    "upload complete" -> {
                                        _uiState.value = ScanBillUIState(
                                            status = ScanBillStatus.ANALYZING,
                                            isSubmitAble = false,
                                            progress = it.progress,
                                            detail = ""
                                        )
                                    }
                                    else -> {
                                        _uiState.value = ScanBillUIState(
                                            status = ScanBillStatus.DONE,
                                            isSubmitAble = true,
                                            progress = 0.0,
                                            detail = it.detail.toString()
                                        )
                                    }
                                }
                            }
                            .onFailure {
                                _uiState.value = ScanBillUIState(
                                    status = ScanBillStatus.ERROR,
                                    isSubmitAble = true,
                                    progress = 0.0,
                                    detail = it.message ?: ""
                                )
                            }
                    }
            }
        }
    }

    fun confirmCancel() {
        uploadJob?.cancel()
        when (_uiState.value.status) {
            ScanBillStatus.LOADING -> {
                viewModelScope.launch {
                    _uiState.value = ScanBillUIState(
                        status = ScanBillStatus.CANCELING,
                        isSubmitAble = false,
                        progress = 0.0,
                        detail = ""
                    )
                    delay(500)
                    billReceiptRepository.cancelChunkUploadBillReceipt()
                        ?.onSuccess {
                            _uiState.value = ScanBillUIState(
                                status = ScanBillStatus.DONE,
                                isSubmitAble = true,
                                progress = 0.0,
                                detail = ""
                            )
                        }
                        ?.onFailure {
                            _uiState.value = ScanBillUIState(
                                status = ScanBillStatus.ERROR,
                                isSubmitAble = true,
                                progress = 0.0,
                                detail = it.message ?: ""
                            )
                        }
                }
            }
            ScanBillStatus.ANALYZING -> {
                viewModelScope.launch {
                    billReceiptRepository.deleteUploadedFile("")
                        ?.onSuccess {
                            _uiState.value = ScanBillUIState(
                                status = ScanBillStatus.DONE,
                                isSubmitAble = true,
                                progress = 0.0,
                                detail = ""
                            )
                        }
                        ?.onFailure {
                            _uiState.value = ScanBillUIState(
                                status = ScanBillStatus.ERROR,
                                isSubmitAble = true,
                                progress = 0.0,
                                detail = it.message ?: ""
                            )
                        }
                }
            }
            else -> {
                _uiState.value = ScanBillUIState(
                    status = ScanBillStatus.DONE,
                    isSubmitAble = true,
                    progress = 0.0,
                    detail = ""
                )
            }
        }
    }

    init {
        Log.d("viewModel", "ScanViewModel is initiated")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("viewModel", "ScanViewModel is onCleared")
    }

}