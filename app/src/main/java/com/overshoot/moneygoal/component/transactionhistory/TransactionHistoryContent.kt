package com.overshoot.moneygoal.component.transactionhistory

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.overshoot.moneygoal.R
import com.overshoot.moneygoal.component.home.uistatemodel.TransactionUIState
import com.overshoot.moneygoal.component.transactionhistory.stateholder.viewmodel.TransactionHistoryViewModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TransactionHistoryContent() {

    val transactionHistoryViewModel = koinViewModel<TransactionHistoryViewModel>()

    LaunchedEffect(key1 = Unit) {
        transactionHistoryViewModel.subscribe()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Transaction History",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Badge(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                    Box(modifier = Modifier.padding(4.dp)) {
                        Text(
                            text = transactionHistoryViewModel.incomeCount.intValue.toString(),
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Badge(containerColor = MaterialTheme.colorScheme.errorContainer) {
                    Box(modifier = Modifier.padding(4.dp)) {
                        Text(
                            text = transactionHistoryViewModel.expenseCount.intValue.toString(),
                            fontSize = 14.sp
                        )
                    }
                }
            }
            LazyColumn {
                items(transactionHistoryViewModel.transaction) {
                    TransactionItem(item = it)
                }
            }
        }
    }
}

@Composable
private fun TransactionItem(item: TransactionUIState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${item.name}")
                Text(
                    text = "${item.value} ${stringResource(id = R.string.money_unit)}",
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "category")
                Text(
                    text = "${item.type}",
                    color = if (item.type == "Income") Color.Green else Color.Red
                )
            }
        }
    }
}