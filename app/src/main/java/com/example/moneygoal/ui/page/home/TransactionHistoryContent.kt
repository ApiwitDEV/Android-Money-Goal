package com.example.moneygoal.ui.page.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TransactionHistoryContent() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Text(text = "History")
        }
    }
}