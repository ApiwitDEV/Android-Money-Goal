package com.example.moneygoal.ui.page.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moneygoal.ui.theme.MoneyGoalTheme
import com.example.moneygoal.viewmodel.GoalViewModel

@Composable
fun HomeScreen(
    goalViewModel: GoalViewModel,
    onGoto: () -> Unit
) {
    HomeContent(
        x = goalViewModel.x.value,
        onAddGoal = {
            goalViewModel.addGoal()
        },
        onGoto = {
            onGoto()
        }
    )
}

@Composable
private fun HomeContent(x: Int, onAddGoal: () -> Unit, onGoto: () -> Unit) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Greeting("Android$x")
            Content {
                onAddGoal()
            }
            Card(
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(color = Color.Cyan)
                        .clickable {
                            onGoto()
                        }
                        .padding(all = 20.dp)
                ) {
                    Text(text = "ADD Goal")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
private fun Content(onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .background(color = Color.Cyan)
                .clickable {
                    onClick()
                }
                .padding(all = 20.dp)
        ) {
            Text(text = "ADD Goal")
        }
    }
}

@Composable
@Preview
private fun PreviewHomeContent() {
    MoneyGoalTheme {
        HomeContent(5, {}, {})
    }
}