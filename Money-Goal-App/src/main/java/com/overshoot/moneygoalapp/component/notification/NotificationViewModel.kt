package com.overshoot.moneygoalapp.component.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationViewModel: ViewModel() {

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    fun showDialog() {
        _showDialog.value = true
    }

    fun hideDialog() {
        _showDialog.value = false
    }

}