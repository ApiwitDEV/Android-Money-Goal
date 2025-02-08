package com.overshoot.moneygoalapp.component.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.overshoot.moneygoalapp.R
import com.overshoot.moneygoalapp.component.home.uistatemodel.GoalItemUIState
import com.overshoot.moneygoalapp.component.home.uistatemodel.GoalPeriodItemUIState
import com.overshoot.moneygoalapp.theme.MoneyGoalTheme

@Composable
fun HomeScreen(
    goToDashboard: () -> Unit,
    openGoalSheet: () -> Unit,
    goalPeriodList: List<GoalPeriodItemUIState>,
    successGoalList: List<GoalItemUIState>,
    failGoalList: List<GoalItemUIState>,
    selectedGoalPeriod: String,
    onSelectPeriod: (String) -> Unit,
    onClickGoal: (Int) -> Unit,
    openAddTransactionSheet: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { openAddTransactionSheet() },
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 2.dp)
            ) {
                Image(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(32.dp),
                    painter = painterResource(id = R.drawable.baseline_add_24),
                    contentDescription = "add goal"
                )
            }
        }
    ) { paddingValues ->
        paddingValues
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SummaryComponent(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = goToDashboard
            )
            GoalPeriod(selectedGoalPeriod = selectedGoalPeriod, goalPeriodList = goalPeriodList, onSelectPeriod = onSelectPeriod)
            SuccessGoalListContent(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp),
                openGoalSheet = openGoalSheet,
                successGoalList = successGoalList,
                onClick = onClickGoal
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewHomeDetailContent() {
    MoneyGoalTheme {

    }
}