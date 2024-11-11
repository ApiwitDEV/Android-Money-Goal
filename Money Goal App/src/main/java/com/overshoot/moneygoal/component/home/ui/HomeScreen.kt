package com.overshoot.moneygoal.component.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.overshoot.moneygoal.R
import com.overshoot.moneygoal.component.home.uistatemodel.GoalItemUIState
import com.overshoot.moneygoal.component.home.uistatemodel.GoalPeriodItemUIState
import com.overshoot.moneygoal.theme.MoneyGoalTheme

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
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clickable { openAddTransactionSheet() }
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