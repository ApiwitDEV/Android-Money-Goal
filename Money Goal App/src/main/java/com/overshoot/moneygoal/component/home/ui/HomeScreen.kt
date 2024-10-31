package com.overshoot.moneygoal.component.home.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.overshoot.moneygoal.R
import com.overshoot.moneygoal.common.ui.LoadingDialog
import com.overshoot.moneygoal.component.account.AccountContent
import com.overshoot.moneygoal.component.home.HomeContentType
import com.overshoot.moneygoal.component.home.stateholder.GoalListStateHolder
import com.overshoot.moneygoal.theme.MoneyGoalTheme
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.HomeGoalDetailViewModel
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.HomeTransactionViewModel
import com.overshoot.moneygoal.component.home.uistatemodel.AddGoalResultUIState
import com.overshoot.moneygoal.component.home.uistatemodel.GoalItemUIState
import com.overshoot.moneygoal.component.home.uistatemodel.GoalPeriodItemUIState
import com.overshoot.moneygoal.component.scanbill.ui.ScanContent
import com.overshoot.moneygoal.component.transactionhistory.TransactionHistoryContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeGoalDetailViewModel: HomeGoalDetailViewModel,
    homeTransactionViewModel: HomeTransactionViewModel,
    onGoto: () -> Unit,
    onBackPressed: () -> Unit,
    onSignOut: () -> Unit
) {
    val selected = remember { mutableStateOf(HomeContentType.HomeContent) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val showBottomSheet = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val isLoading = homeTransactionViewModel.addTransactionLoading.collectAsStateWithLifecycle().value ||
            homeGoalDetailViewModel.addGoalLoading.collectAsStateWithLifecycle().value
    var sheetType by remember { mutableStateOf<SheetType?>(null) }

    val goalListStateHolder = remember { GoalListStateHolder() }

    BackHandler {
        onBackPressed()
    }

    LaunchedEffect(key1 = null) {
        goalListStateHolder.collectGoalPeriodListState(
            state = homeGoalDetailViewModel.goalPeriodList
        )
    }

    LaunchedEffect(key1 = null) {
        goalListStateHolder.collectGoalListState(state = homeGoalDetailViewModel.allGoal)
    }

    LaunchedEffect(key1 = null) {
        homeGoalDetailViewModel.observeGoalPeriodList()
        homeGoalDetailViewModel.observeAllGoal()
    }

    HomeContent(
        sheetState = sheetState,
        showBottomSheet = showBottomSheet.value,
        selected = selected.value,
        goalPeriodList = goalListStateHolder.goalPeriodItemUIStateList,
        selectedGoalPeriod = goalListStateHolder.selectedGoalPeriod.value,
        onSelectPeriod = {
            goalListStateHolder.onSelectPeriod(it)
            goalListStateHolder.showGoalByPeriod(it)
        },
        successGoalList = goalListStateHolder.goalListToShow,
        failGoalList = goalListStateHolder.failGoalList,
        onSelectContent = {
            selected.value = it
        },
        openAddGoalSheet = {
            sheetType = SheetType.AddGoalSheet
            showBottomSheet.value = true
            homeGoalDetailViewModel.allGoal
            scope.launch {
                sheetState.show()
            }
        },
        onGoto = {
            onGoto()
        },
        openAddTransactionSheet = {
            homeTransactionViewModel.getAllCategory()
            sheetType = SheetType.AddTransactionSheet
            showBottomSheet.value = true
            scope.launch {
                sheetState.show()
            }
        },
        onClickGoal = {
            goalListStateHolder.onClickGoal(it)
        },
        onSignOut = onSignOut
    )

    LaunchedEffect(key1 = Unit) {
        homeGoalDetailViewModel.addGoalResult.collect {
            when(it) {
                AddGoalResultUIState.SUCCESS -> Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
                AddGoalResultUIState.FAILURE -> Toast.makeText(context, "Failure", Toast.LENGTH_LONG).show()
                null -> {}
            }
        }
    }

    if (showBottomSheet.value) {
        when(sheetType) {
            SheetType.AddTransactionSheet -> {
                AddTransactionBottomSheet(
                    sheetState = sheetState,
                    categoryList = homeTransactionViewModel.category.collectAsStateWithLifecycle().value,
                    onCloseBottomSheet = {
                        scope.launch {
                            sheetState.hide()
                        }
                        showBottomSheet.value = false
                        sheetType = null
                    },
                    onAddTransaction = {
                        scope.launch {
                            sheetState.hide()
                        }
                        showBottomSheet.value = false
                        sheetType = null
                        homeTransactionViewModel.addTransaction(transaction = it)
                    }
                )
            }
            SheetType.AddGoalSheet -> {
                AddGoalBottomSheet(
                    sheetState = sheetState,
                    onCloseBottomSheet = {
                        scope.launch {
                            sheetState.hide()
                        }
                        showBottomSheet.value = false
                        sheetType = null
                    },
                    onAddGoal = { goalName, goalObjective, period, targetValue ->
                        homeGoalDetailViewModel.addGoal(goalName, goalObjective, period, targetValue)
                    }
                )
            }
            null -> {

            }
        }
    }

    if (isLoading) {
        LoadingDialog()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    sheetState: SheetState,
    showBottomSheet: Boolean,
    selected: HomeContentType,
    goalPeriodList: List<GoalPeriodItemUIState>,
    selectedGoalPeriod: String,
    onSelectPeriod: (String) -> Unit,
    successGoalList: List<GoalItemUIState>,
    failGoalList: List<GoalItemUIState>,
    onSelectContent: (HomeContentType) -> Unit,
    openAddGoalSheet: () -> Unit,
    onGoto: () -> Unit,
    openAddTransactionSheet: () -> Unit,
    onClickGoal: (Int) -> Unit,
    onSignOut: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.Gray.copy(alpha = 0.1f),
        floatingActionButton = {
            if (selected == HomeContentType.HomeContent) {
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
        },
        content = {
            AnimatedVisibility(
                modifier = Modifier.padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding()),
                visible = selected == HomeContentType.HomeContent,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                HomeDetailContent(
                    onGoto = onGoto,
                    openGoalSheet = openAddGoalSheet,
                    goalPeriodList = goalPeriodList,
                    successGoalList = successGoalList,
                    failGoalList = failGoalList,
                    selectedGoalPeriod = selectedGoalPeriod,
                    onSelectPeriod = onSelectPeriod,
                    onClickGoal = onClickGoal
                )
            }
            AnimatedVisibility(
                visible = selected == HomeContentType.ScanContent,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ScanContent(it)
            }
            AnimatedVisibility(
                modifier = Modifier.padding(bottom = it.calculateBottomPadding()),
                visible = selected == HomeContentType.TransactionHistoryContent,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                TransactionHistoryContent()
            }
            AnimatedVisibility(
                visible = selected == HomeContentType.AccountContent,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                AccountContent(signOut = onSignOut)
            }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .height(64.dp)
                    .fillMaxWidth(),
                contentColor = Color.Gray,
                content = {
                    this.NavigationBarItem(
                        selected = selected == HomeContentType.HomeContent,
                        onClick = {
                            onSelectContent(HomeContentType.HomeContent)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_home_24),
                                contentDescription = "home"
                            )
                        }
                    )
                    this.NavigationBarItem(
                        selected = selected == HomeContentType.ScanContent,
                        onClick = {
                            onSelectContent(HomeContentType.ScanContent)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_document_scanner_24),
                                contentDescription = "home"
                            )
                        }
                    )
                    this.NavigationBarItem(
                        selected = selected == HomeContentType.TransactionHistoryContent,
                        onClick = {
                            onSelectContent(HomeContentType.TransactionHistoryContent)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_history_24),
                                contentDescription = "history"
                            )
                        }
                    )
                    this.NavigationBarItem(
                        selected = selected == HomeContentType.AccountContent,
                        onClick = {
                            onSelectContent(HomeContentType.AccountContent)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_account_circle_24),
                                contentDescription = "account"
                            )
                        }
                    )
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun PreviewHomeContent() {
    MoneyGoalTheme {
        HomeContent(
            sheetState = rememberModalBottomSheetState(),
            showBottomSheet = false,
            selected = HomeContentType.HomeContent,
            selectedGoalPeriod = "",
            onSelectPeriod = {},
            goalPeriodList = listOf(),
            successGoalList = listOf(),
            failGoalList = listOf(),
            onSelectContent = {},
            openAddGoalSheet = {},
            onGoto = {},
            openAddTransactionSheet = {},
            onClickGoal = {},
            onSignOut = {}
        )
    }
}