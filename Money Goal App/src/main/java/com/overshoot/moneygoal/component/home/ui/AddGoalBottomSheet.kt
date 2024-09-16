package com.overshoot.moneygoal.component.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.overshoot.moneygoal.R
import com.overshoot.moneygoal.common.Period

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddGoalBottomSheet(
    sheetState: SheetState,
    onCloseBottomSheet: () -> Unit,
    onAddGoal: (String, String, String, Double) -> Unit
) {
    var goalName by remember { mutableStateOf("") }
    var targetValue by remember { mutableStateOf("") }
    var goalObjective by remember { mutableStateOf(GoalObjective.Limit) }
    var expand by remember { mutableStateOf(false) }
    var period by remember { mutableStateOf("") }
    ModalBottomSheet(
        modifier = Modifier.navigationBarsPadding(),
        onDismissRequest = {
            onCloseBottomSheet()
        },
        sheetState = sheetState
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = goalName,
                onValueChange = {
                    goalName = it
                },
                placeholder = {
                    Text(text = "Goal Name")
                }
            )
            Row(modifier = Modifier.clickable { expand = !expand }) {
                Icon(painter = painterResource(id = R.drawable.baseline_add_24), contentDescription = null)
                Text(text = "Tag")
                DropdownMenu(expanded = expand, onDismissRequest = {
                    expand = !expand
                }) {
                    DropdownMenuItem(text = { Text(text = "Daily") }, onClick = { period = Period.DAILY.value; expand = !expand })
                    DropdownMenuItem(text = { Text(text = "Weekly") }, onClick = { period = Period.WEEKLY.value; expand = !expand })
                    DropdownMenuItem(text = { Text(text = "Monthly") }, onClick = { period = Period.MONTHLY.value; expand = !expand })
                }
                SwitchButton(
                    goalObjective = goalObjective,
                    onChange = {
                        goalObjective = it
                    }
                )
            }
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = targetValue,
                onValueChange = {
                    targetValue = it
                },
                placeholder = {
                    Text(
                        text = when(goalObjective) {
                        GoalObjective.Limit -> "Limit Money"
                        GoalObjective.Target -> "Target Money"
                    })
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
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
                        onAddGoal(
                            goalName,
                            goalObjective.toString(),
                            period,
                            try { targetValue.toDouble() } catch (e: Exception) { 0.0 }
                        )
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
private fun SwitchButton(goalObjective: GoalObjective, onChange: (GoalObjective) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Button(
            onClick = { onChange(GoalObjective.Limit) },
            colors = if (goalObjective == GoalObjective.Limit) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer) else ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text(text = "Limit")
        }
        Button(
            onClick = { onChange(GoalObjective.Target) },
            colors = if (goalObjective == GoalObjective.Target) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer) else ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text(text = "Target")
        }
    }
}

 enum class GoalObjective {
     Limit,
     Target
 }