package com.overshoot.moneygoal.component.home.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.overshoot.moneygoal.component.home.uistatemodel.GoalPeriodItemUIState

@Composable
fun GoalPeriod(selectedGoalPeriod: String, goalPeriodList: List<GoalPeriodItemUIState>, onSelectPeriod: (String) -> Unit) {
    LazyRow {
        itemsIndexed(goalPeriodList) { index, goalPeriodItem ->
            Spacer(
                modifier = Modifier.width(
                    if (index == 0) {
                        16.dp
                    }
                    else {
                        8.dp
                    }
                )
            )
            GoalPeriodItem(item = goalPeriodItem, selected = selectedGoalPeriod, onClick = onSelectPeriod)
            if (index == goalPeriodList.size - 1) {
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

@Composable
private fun GoalPeriodItem(item: GoalPeriodItemUIState, selected: String, onClick: (String) -> Unit) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = if(selected == item.period) Color.Unspecified else Color.Gray.copy(alpha = 0.25f)
        ),
        onClick = { onClick(item.period?:"") }
    ) {
        Text(text = "${item.period} (${item.count})")
    }
}