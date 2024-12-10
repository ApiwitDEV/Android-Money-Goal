package com.overshoot.moneygoalapp.component.transactionhistory

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.overshoot.moneygoalapp.R
import com.overshoot.moneygoalapp.common.ui.LoadingDialog
import com.overshoot.moneygoalapp.component.home.uistatemodel.TransactionUIState
import com.overshoot.moneygoalapp.component.transactionhistory.stateholder.viewmodel.TransactionHistoryViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TransactionHistoryScreen(
    transactionHistoryViewModel: TransactionHistoryViewModel = koinViewModel()
) {

    val isSelectMode = transactionHistoryViewModel.isSelectMode.value
    val transactions = transactionHistoryViewModel.transaction

    LaunchedEffect(null) {
        transactionHistoryViewModel.subscribe()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (transactionHistoryViewModel.isLoading.value) {
            LoadingDialog()
        }
        else {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.height(48.dp),
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
                        if (!transactionHistoryViewModel.haveClickedItem.value && isSelectMode) {
                            TextButton(onClick = transactionHistoryViewModel::onSelectAll) {
                                Text(
                                    text = "All",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            TextButton(onClick = transactionHistoryViewModel::clearAllSelectedStatus) {
                                Text(
                                    text = "Clear",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        else if (!transactionHistoryViewModel.haveClickedItem.value) {
                            TextButton(onClick = transactionHistoryViewModel::onSelectMode) {
                                Text(
                                    text = "Select",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    LazyColumn {
                        items(transactions) { item ->
                            TransactionItem(
                                item = item,
                                isSelectMode = isSelectMode,
                                onSelectItem = { id ->
                                    transactionHistoryViewModel.onSelectItem(id)
                                },
                                onClick = { id ->
                                    transactionHistoryViewModel.onClickToEdit(id)
                                },
                                onEditItem = { id ->
                                    transactionHistoryViewModel.onEditTransaction(id)
                                }
                            )
                        }
                    }
                }
                if (transactionHistoryViewModel.haveSelectedItem.value) {
                    Row(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            onClick = {
                                transactionHistoryViewModel.cancelDelete()
                            }
                        ) {
                            Text(text = "Cancel", color = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            onClick = {
                                transactionHistoryViewModel.deleteTransaction()
                            }
                        ) {
                            Text(text = "Delete", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionItem(
    item: TransactionUIState,
    isSelectMode: Boolean,
    onSelectItem: (String) -> Unit,
    onClick: (String) -> Unit,
    onEditItem: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = isSelectMode
        ) {
            Checkbox(
                checked = item.isSelected,
                onCheckedChange = {
                    onSelectItem(item.id)
                }
            )
        }
        AnimatedVisibility(
            visible =  item.isEdit
        ) {
            IconButton(onClick = { onEditItem(item.id) }) {
                Icon(painter = painterResource(id = R.drawable.baseline_edit_24), contentDescription = null)
            }
        }
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
            ,
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .clickable {
                        if (!isSelectMode) onClick(item.id)
                        else onSelectItem(item.id)
                    }
//                        .animateContentSize()
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
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
                    Text(text = item.category.toString())
                    Text(
                        text = "${item.type}",
                        color = if (item.type == "Income") Color.Green else Color.Red
                    )
                }
                if (!item.remark.isNullOrBlank()) {
                    Text(
                        text = "* "+item.remark,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}