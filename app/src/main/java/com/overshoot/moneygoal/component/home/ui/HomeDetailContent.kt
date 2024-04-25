package com.overshoot.moneygoal.component.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.overshoot.moneygoal.component.home.uistatemodel.GoalItemUIState
import com.overshoot.moneygoal.theme.MoneyGoalTheme
import java.time.YearMonth

@Composable
fun HomeDetailContent(
    onGoto: () -> Unit,
    successGoalList: List<GoalItemUIState>,
    failGoalList: List<GoalItemUIState>
) {
    Column(
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SummaryComponent(
            modifier = Modifier,
            onClick = onGoto
        )
        SuccessGoalListContent(
            modifier = Modifier.fillMaxHeight(),
            successGoalList = successGoalList
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewHomeDetailContent() {
    MoneyGoalTheme {
        HomeDetailContent(
            onGoto = {},
            successGoalList = listOf(),
            failGoalList = listOf()
        )
    }
}