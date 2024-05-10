package com.overshoot.moneygoal.component.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.overshoot.moneygoal.component.home.uistatemodel.GoalItemUIState
import com.overshoot.moneygoal.component.home.uistatemodel.GoalPeriodItemUIState
import com.overshoot.moneygoal.theme.MoneyGoalTheme

@Composable
fun HomeDetailContent(
    onGoto: () -> Unit,
    openGoalSheet: () -> Unit,
    goalPeriodList: List<GoalPeriodItemUIState>,
    successGoalList: List<GoalItemUIState>,
    failGoalList: List<GoalItemUIState>,
    selectedGoalPeriod: String,
    onSelectPeriod: (String) -> Unit,
    onClickGoal: (Int) -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SummaryComponent(
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = onGoto
        )
        GoalPeriod(selectedGoalPeriod = selectedGoalPeriod, goalPeriodList = goalPeriodList, onSelectPeriod = onSelectPeriod)
        SuccessGoalListContent(
            modifier = Modifier.fillMaxHeight().padding(horizontal = 16.dp),
            openGoalSheet = openGoalSheet,
            successGoalList = successGoalList,
            onClick = onClickGoal
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewHomeDetailContent() {
    MoneyGoalTheme {
        HomeDetailContent(
            onGoto = {},
            openGoalSheet = {},
            goalPeriodList = listOf(),
            successGoalList = listOf(),
            failGoalList = listOf(),
            selectedGoalPeriod = "",
            onSelectPeriod = {},
            onClickGoal = {}
        )
    }
}