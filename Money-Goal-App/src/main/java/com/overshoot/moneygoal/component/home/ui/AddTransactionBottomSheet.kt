package com.overshoot.moneygoal.component.home.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.overshoot.moneygoal.R
import com.overshoot.moneygoal.component.home.uistatemodel.CategoryUIState
import com.overshoot.moneygoal.component.home.upstreamdatamodel.AddTransactionData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddTransactionBottomSheet(
    sheetState: SheetState,
    categoryList: List<CategoryUIState>,
    onCloseBottomSheet: () -> Unit,
    onAddTransaction: (AddTransactionData) -> Unit
) {
    var expand by remember { mutableStateOf(false) }
    var transactionName by remember { mutableStateOf("") }
    var transactionRemark by remember { mutableStateOf("") }
    var transactionValue by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf("") }
    var selectedCategoryName by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf("Expense") }
    var buttonExpand by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        modifier = Modifier.navigationBarsPadding(),
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
                Row(modifier = Modifier.clickable { expand = true }) {
                    Icon(painter = painterResource(id = R.drawable.baseline_add_24), contentDescription = null)
                    Text(text = selectedCategoryName.ifEmpty { "Category" })
                    DropdownMenu(expanded = expand, onDismissRequest = {
                        expand = false
                    }) {
                        categoryList.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(text = "id : ${category.id} name : ${category.name}") },
                                onClick = {
                                    selectedCategoryId = category.id
                                    selectedCategoryName = category.name
                                    expand = false
                                }
                            )
                        }
                    }
                }
                Text(
                    modifier = Modifier.clickable {
                        transactionName = ""
                        transactionRemark = ""
                        transactionValue = ""
                    },
                    text = "Clear"
                )
            }
            SlideButton(
                selected = selected,
                expand = buttonExpand,
                onSelect = {
                    if (it == "Income") {
                        scope.launch {
                            buttonExpand = true
                            delay(200)
                            selected = "Income"
                            buttonExpand = false
                        }
                    }
                    else if (it == "Expense"){
                        scope.launch {
                            buttonExpand = true
                            delay(200)
                            selected = "Expense"
                            buttonExpand = false
                        }
                    }
                }
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = transactionName,
                onValueChange = {
                    transactionName = it
                },
                placeholder = {
                    Text(text = "Transaction Name*")
                },
                keyboardActions = KeyboardActions(onNext = {})
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = transactionValue,
                onValueChange = {
                    transactionValue = it
                },
                placeholder = {
                    Text(text = "$selected*")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                keyboardActions = KeyboardActions(onDone = {})
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = transactionRemark,
                onValueChange = {
                    transactionRemark = it
                },
                placeholder = {
                    Text(text = "Remark")
                },
                keyboardActions = KeyboardActions(onNext = {})
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
                    onClick = {
                        if (
                            transactionName.isNotBlank() &&
                            transactionValue.isNotBlank() &&
                            selectedCategoryId != "" &&
                            selectedCategoryName.isNotBlank()
                            ) {
                            onAddTransaction(
                                AddTransactionData(
                                    name = transactionName,
                                    categoryId = selectedCategoryId,
                                    remark = transactionRemark,
                                    type = selected,
                                    value = transactionValue.toDouble()
                                )
                            )
                        }
                        else {

                        }
                    }
                ) {
                    Text(text = "Submit")
                }
            }
        }
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun SlideButton(selected: String,expand: Boolean, onSelect:(String) -> Unit) {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.inverseOnSurface, shape = CircleShape)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                .align(if (selected == "Expense") Alignment.CenterStart else Alignment.CenterEnd),
        ) {
            Box(
                modifier = Modifier
                    .animateContentSize()
                    .height(40.dp)
                    .fillMaxWidth(if (expand) 1f else 0.5f)
            ) {}
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onSelect("Expense")
                },
                text = "Expense",
                color = if (selected == "Expense") Color.White else Color.Black
            )
            Text(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onSelect("Income")
                },
                text = "Income",
                color = if (selected == "Income") Color.White else Color.Black
            )
        }
    }
}