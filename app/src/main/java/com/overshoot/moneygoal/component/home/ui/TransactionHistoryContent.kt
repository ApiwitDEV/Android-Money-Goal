package com.overshoot.moneygoal.component.home.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.overshoot.moneygoal.component.home.uistatemodel.TransactionUIState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TransactionHistoryContent(transactionList: List<TransactionUIState>) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Text(text = "History")
            LazyColumn {
                items(transactionList) {
                    Text(text = "${it.name} ${it.value}")
                }
            }
        }
    }
}