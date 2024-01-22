package com.overshoot.moneygoal

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.overshoot.moneygoal.navigation.NavigationHost
import com.overshoot.moneygoal.theme.MoneyGoalTheme
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.GoalViewModel
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.TransactionViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val goalViewModel by viewModel<GoalViewModel>()
    private val transactionViewModel by viewModel<TransactionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goalViewModel.error.observe(this) {
            Toast.makeText(baseContext, goalViewModel.error.value, Toast.LENGTH_SHORT).show()
        }
        setContent {
            MoneyGoalTheme {
                //A surface container using the 'background' color from the theme
                NavigationHost(
                    goalViewModel = goalViewModel,
                    transactionViewModel = transactionViewModel
                )
            }
        }
    }
}