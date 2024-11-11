package com.overshoot.moneygoal.component.home.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.overshoot.domain.usecase.initial.LoadAllInitialDataUseCase
import com.overshoot.moneygoal.AppStateHolder
import com.overshoot.moneygoal.R
import com.overshoot.moneygoal.common.ui.LoadingDialog
import com.overshoot.moneygoal.component.home.stateholder.GoalListStateHolder
import com.overshoot.moneygoal.theme.MoneyGoalTheme
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.HomeGoalDetailViewModel
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.HomeTransactionViewModel
import com.overshoot.moneygoal.component.home.uistatemodel.AddGoalResultUIState
import com.overshoot.moneygoal.navigation.main.MainNavigationHost
import com.overshoot.moneygoal.navigation.main.MainNavigationRoute
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun MainScreen(
    appStateHolder: AppStateHolder,
    onBackPressed: () -> Unit
) {
    val homeGoalDetailViewModel: HomeGoalDetailViewModel = koinViewModel()
    val homeTransactionViewModel: HomeTransactionViewModel = koinViewModel()
    val loadAllInitialDataUseCase = koinInject<LoadAllInitialDataUseCase>()
    var selected by remember { mutableStateOf(MainNavigationRoute.Home) }
    val context = LocalContext.current
    val isLoading = homeTransactionViewModel.addTransactionLoading.collectAsStateWithLifecycle().value ||
            homeGoalDetailViewModel.addGoalLoading.collectAsStateWithLifecycle().value

    val goalListStateHolder = remember { GoalListStateHolder() }

    BackHandler {
        onBackPressed()
    }

    LaunchedEffect(null) {
        loadAllInitialDataUseCase.invoke()
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

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.Gray.copy(alpha = 0.1f),
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding()
                    )
            ) {
                MainContent(
                    appStateHolder = appStateHolder,
                    homeGoalDetailViewModel = homeGoalDetailViewModel,
                    homeTransactionViewModel = homeTransactionViewModel
                )
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
                        selected = selected == MainNavigationRoute.Home,
                        onClick = {
                            selected = MainNavigationRoute.Home
                            appStateHolder.clearBackStackAndNavigateTo(MainNavigationRoute.Home)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_home_24),
                                contentDescription = "home"
                            )
                        }
                    )
                    this.NavigationBarItem(
                        selected = selected == MainNavigationRoute.ScanBill,
                        onClick = {
                            selected = MainNavigationRoute.ScanBill
                            appStateHolder.clearBackStackAndNavigateTo(MainNavigationRoute.ScanBill)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_document_scanner_24),
                                contentDescription = "home"
                            )
                        }
                    )
                    this.NavigationBarItem(
                        selected = selected == MainNavigationRoute.TransactionHistory,
                        onClick = {
                            selected = MainNavigationRoute.TransactionHistory
                            appStateHolder.clearBackStackAndNavigateTo(MainNavigationRoute.TransactionHistory)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_history_24),
                                contentDescription = "history"
                            )
                        }
                    )
                    this.NavigationBarItem(
                        selected = selected == MainNavigationRoute.Account,
                        onClick = {
                            selected = MainNavigationRoute.Account
                            appStateHolder.clearBackStackAndNavigateTo(MainNavigationRoute.Account)
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

    LaunchedEffect(key1 = Unit) {
        homeGoalDetailViewModel.addGoalResult.collect {
            when(it) {
                AddGoalResultUIState.SUCCESS -> Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
                AddGoalResultUIState.FAILURE -> Toast.makeText(context, "Failure", Toast.LENGTH_LONG).show()
                null -> {}
            }
        }
    }

    if (isLoading) {
        LoadingDialog()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
    appStateHolder: AppStateHolder,
    homeGoalDetailViewModel: HomeGoalDetailViewModel,
    homeTransactionViewModel: HomeTransactionViewModel
) {

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var sheetType by remember { mutableStateOf<SheetType?>(null) }
    val showBottomSheet = remember { mutableStateOf(false) }

    MainNavigationHost(
        appStateHolder = appStateHolder,
        openAddGoalSheet = {
            sheetType = SheetType.AddGoalSheet
            showBottomSheet.value = true
            homeGoalDetailViewModel.allGoal
            scope.launch {
                sheetState.show()
            }
        },
        openAddTransactionSheet = {
            homeTransactionViewModel.getAllCategory()
            sheetType = SheetType.AddTransactionSheet
            showBottomSheet.value = true
            scope.launch {
                sheetState.show()
            }
        }
    )
    if (showBottomSheet.value) {
        when(sheetType) {
            SheetType.AddTransactionSheet -> {
                AddTransactionBottomSheet(
                    sheetState = sheetState,
                    categoryList = homeTransactionViewModel.categoryList.collectAsStateWithLifecycle().value,
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
}

@Composable
@Preview
private fun PreviewHomeContent() {
    MoneyGoalTheme {
    }
}