package com.overshoot.moneygoal.component.dashboard

import android.annotation.SuppressLint
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreen() {
    Scaffold(containerColor = MaterialTheme.colorScheme.secondaryContainer) {
        Text(text = "Dashboard")
    }
}