package com.overshoot.moneygoal.component.home.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.overshoot.moneygoal.R
import com.overshoot.moneygoal.common.ui.LoadingDialog
import com.overshoot.moneygoal.component.home.HomeContentType
import com.overshoot.moneygoal.theme.MoneyGoalTheme
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.GoalViewModel
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    goalViewModel: GoalViewModel,
    transactionViewModel: TransactionViewModel,
    onGoto: () -> Unit
) {
    val selected = remember { mutableStateOf(HomeContentType.HomeContent) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val showBottomSheet = remember { mutableStateOf(false) }
    val transactionName = remember { mutableStateOf("") }
    val transactionRemark = remember { mutableStateOf("") }
    val transactionExpense = remember { mutableStateOf("") }
    val expand = remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        transactionViewModel.subscribe()
    }

    LaunchedEffect(key1 = transactionViewModel.transaction.value, key2 = transactionViewModel.isLoading.value) {
        if (!transactionViewModel.isLoading.value) {
            Toast.makeText(context, transactionViewModel.transaction.value.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(key1 = transactionViewModel.error.value) {
        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(key1 = transactionViewModel.isConnectingLost.value) {
        Toast.makeText(context, "connecting lost", Toast.LENGTH_SHORT).show()
    }

    if (transactionViewModel.isLoading.value) {
        LoadingDialog()
    }

    HomeContent(
        sheetState = sheetState,
        showBottomSheet = showBottomSheet.value,
        selected = selected.value,
        onSelectContent = {
            selected.value = it
        },
        onAddGoal = {
            showBottomSheet.value = true
        },
        onGoto = {
            onGoto()
        },
        expand = expand.value,
        onExpandChange = {
            expand.value = !expand.value
        },
        transactionSuccess = transactionViewModel.addTransactionSuccess.value?: false,
        onCloseBottomSheet = {
            showBottomSheet.value = false
        },
        onAddTransaction = {
            transactionViewModel.addTransaction()
        },
        transactionName = transactionName.value,
        onTransactionNameChange = {
            transactionName.value = it
        },
        transactionRemark = transactionRemark.value,
        onTransactionRemarkChange = {
            transactionRemark.value = it
        },
        transactionExpense = transactionExpense.value,
        onTransactionExpenseChange = {
            transactionExpense.value = it
        },
        onClear = {
            transactionName.value = ""
            transactionExpense.value = ""
            transactionRemark.value = ""
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    sheetState: SheetState,
    showBottomSheet: Boolean,
    selected: HomeContentType,
    onSelectContent: (HomeContentType) -> Unit,
    onAddGoal: () -> Unit,
    onGoto: () -> Unit,
    expand: Boolean,
    onExpandChange: () -> Unit,
    transactionSuccess: Boolean,
    onCloseBottomSheet: () -> Unit,
    onAddTransaction: () -> Unit,
    transactionName: String,
    onTransactionNameChange: (String) -> Unit,
    transactionRemark: String,
    onTransactionRemarkChange: (String) -> Unit,
    transactionExpense: String,
    onTransactionExpenseChange: (String) -> Unit,
    onClear: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            if (selected == HomeContentType.HomeContent) {
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { onAddGoal() }
                            .padding(all = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_add_24),
                            contentDescription = "add goal"
                        )
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = "Transaction"
                        )
                    }
                }
            }
        },
        content = {
            AnimatedVisibility(
                visible = selected == HomeContentType.HomeContent,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                HomeDetailContent(onGoto = onGoto)
            }
            AnimatedVisibility(
                visible = selected == HomeContentType.ScanContent,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ScanContent()
            }
            AnimatedVisibility(
                visible = selected == HomeContentType.TransactionHistoryContent,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                TransactionHistoryContent()
            }
            AnimatedVisibility(
                visible = selected == HomeContentType.AccountContent,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                AccountContent()
            }
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        onCloseBottomSheet()
                    },
                    sheetState = sheetState
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(modifier = Modifier.clickable { onExpandChange() }) {
                                Icon(painter = painterResource(id = R.drawable.baseline_add_24), contentDescription = null)
                                Text(text = "Tag")
                                DropdownMenu(expanded = expand, onDismissRequest = {
                                    onExpandChange()
                                }) {
                                    DropdownMenuItem(text = { Text(text = "A") }, onClick = { /*TODO*/ })
                                    DropdownMenuItem(text = { Text(text = "B") }, onClick = { /*TODO*/ })
                                    DropdownMenuItem(text = { Text(text = "C") }, onClick = { /*TODO*/ })
                                }
                            }
                            Text(
                                modifier = Modifier.clickable { onClear() },
                                text = "Clear"
                            )
                        }
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = transactionName,
                            onValueChange = {
                                onTransactionNameChange(it)
                            },
                            placeholder = {
                                Text(text = "Transaction Name")
                            }
                        )
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = transactionRemark,
                            onValueChange = {
                                onTransactionRemarkChange(it)
                            },
                            placeholder = {
                                Text(text = "Remark")
                            }
                        )
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = transactionExpense,
                            onValueChange = {
                                onTransactionExpenseChange(it)
                            },
                            placeholder = {
                                Text(text = "Expense")
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardOptions.Default.keyboardType
                            ),
                            keyboardActions = KeyboardActions(onDone = {})
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                modifier = Modifier
                                    .weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                                onClick = onCloseBottomSheet
                            ) {
                                Text(text = "Cancel")
                            }
                            Button(
                                modifier = Modifier
                                    .weight(1f),
                                onClick = onAddTransaction
                            ) {
                                Text(text = "Submit")
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth(),
                contentColor = Color.Gray,
                content = {
                    this.NavigationBarItem(
                        selected = selected == HomeContentType.HomeContent,
                        onClick = {
                            onSelectContent(HomeContentType.HomeContent)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_home_24),
                                contentDescription = "home"
                            )
                        }
                    )
                    this.NavigationBarItem(
                        selected = selected == HomeContentType.ScanContent,
                        onClick = {
                            onSelectContent(HomeContentType.ScanContent)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_document_scanner_24),
                                contentDescription = "home"
                            )
                        }
                    )
                    this.NavigationBarItem(
                        selected = selected == HomeContentType.TransactionHistoryContent,
                        onClick = {
                            onSelectContent(HomeContentType.TransactionHistoryContent)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_history_24),
                                contentDescription = "history"
                            )
                        }
                    )
                    this.NavigationBarItem(
                        selected = selected == HomeContentType.AccountContent,
                        onClick = {
                            onSelectContent(HomeContentType.AccountContent)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_account_circle_24),
                                contentDescription = "account"
                            )
                        }
                    )
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun PreviewHomeContent() {
    MoneyGoalTheme {
        HomeContent(
            sheetState = rememberModalBottomSheetState(),
            showBottomSheet = false,
            selected = HomeContentType.HomeContent,
            onSelectContent = {},
            onAddGoal = {},
            onGoto = {},
            expand = false,
            onExpandChange = {},
            transactionSuccess = false,
            onCloseBottomSheet = {},
            onAddTransaction = {},
            transactionName = "",
            onTransactionNameChange = {},
            transactionRemark = "",
            onTransactionRemarkChange = {},
            transactionExpense = "",
            onTransactionExpenseChange = {},
            onClear = {}
        )
    }
}