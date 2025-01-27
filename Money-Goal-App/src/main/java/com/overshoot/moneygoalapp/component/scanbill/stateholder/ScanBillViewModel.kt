package com.overshoot.moneygoalapp.component.scanbill.stateholder

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overshoot.data.repository.BillReceiptRepository
import com.overshoot.moneygoalapp.component.scanbill.uistatemodel.ImageInfoUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ScanBillUIState(
    val isLoading: Boolean = false,
    val isSubmitAble: Boolean = true,
    val detail: String = ""
)

class ScanBillViewModel(private val billReceiptRepository: BillReceiptRepository): ViewModel() {

    private val _uiState = MutableStateFlow(ScanBillUIState())
    val uiState = _uiState.asStateFlow()

    private var _imageInfo: ImageInfoUIState? = null

    fun collectImage(image: ImageInfoUIState?) {
        _imageInfo = image
    }

    fun loadCollectedImage(): ImageInfoUIState? {
        return _imageInfo
    }

    fun submitImage() {
        if (_imageInfo?.image != null && _imageInfo?.imageFileName != null && _imageInfo?.mimeType != null) {
            _uiState.value = ScanBillUIState(
                isLoading = true,
                isSubmitAble = false
            )
            viewModelScope.launch {
                billReceiptRepository.resumableSubmitBillReceipt(
                    image = _imageInfo?.image!!,
                    filename = _imageInfo?.imageFileName!!,
                    type = _imageInfo?.mimeType!!
                )
                    .collect { result ->
                        result
                            .onSuccess {
                                _uiState.value = ScanBillUIState(
                                    isLoading = false,
                                    isSubmitAble = true,
                                    detail = it.detail.toString()
                                )
                            }
                            .onFailure {
                                _uiState.value = ScanBillUIState(
                                    isLoading = false,
                                    isSubmitAble = true,
                                    detail = it.message ?: ""
                                )
                            }
                    }
//                billReceiptRepository.submitBillReceipt(
//                    image = _imageInfo?.image!!,
//                    filename = _imageInfo?.imageFileName!!,
//                    type = _imageInfo?.mimeType!!
//                )
//                    .onSuccess {
//                        _uiState.value = ScanBillUIState(
//                            isLoading = false,
//                            isSubmitAble = true,
//                            detail = it.detail?:""
//                        )
//                    }
//                    .onFailure {
//                        _uiState.value = ScanBillUIState(
//                            isLoading = false,
//                            isSubmitAble = true,
//                            detail = it.message ?: ""
//                        )
//                    }
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