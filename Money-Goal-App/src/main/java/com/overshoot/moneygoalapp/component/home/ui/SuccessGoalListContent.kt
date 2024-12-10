package com.overshoot.moneygoalapp.component.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.overshoot.moneygoalapp.component.home.uistatemodel.GoalItemUIState
import com.overshoot.moneygoalapp.theme.MoneyGoalTheme

@Composable
fun SuccessGoalListContent(
    modifier: Modifier = Modifier,
    openGoalSheet: () -> Unit,
    successGoalList: List<GoalItemUIState>,
    onClick: (Int) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.White,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(all = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.align(Alignment.CenterVertically)) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = "Goal",
                        color = Color.Black,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Badge(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(modifier = Modifier.padding(4.dp)) {
                            Text(
                                text = successGoalList.size.toString(),
                                fontSize = 15.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Badge(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ) {
                        Box(modifier = Modifier.padding(4.dp)) {
                            Text(
                                text = "1",
                                fontSize = 15.sp
                            )
                        }
                    }
                }
                Button(onClick = openGoalSheet) {
                    Text(text = "Add Goal")
                }
            }
            LazyColumn {
                itemsIndexed(successGoalList) { index, item ->
                    GoalItem(item = item, onClick)
//                if (index != successGoalList.size - 1) {
//                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
//                }
                }
            }
        }
    }
}

@Composable
private fun GoalItem(item: GoalItemUIState, onClick: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable {
                onClick(item.id)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.name,
                color = Color.Black
            )
            Text(
                text = item.objective,
                color = Color.Black
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "0",
                color = Color.Black
            )
            Text(
                text = item.target.toString(),
                color = Color.Black
            )
        }
        LinearProgressIndicator(
            progress = { 0.5f },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
@Preview
fun SuccessGoalListContentPreview() {
    MoneyGoalTheme {
        SuccessGoalListContent(openGoalSheet = {},successGoalList = listOf(), onClick = {})
    }
}