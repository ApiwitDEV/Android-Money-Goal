package com.overshoot.moneygoal.component.home.stateholder

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.overshoot.moneygoal.component.home.uistatemodel.GoalItemUIState
import com.overshoot.moneygoal.component.home.uistatemodel.GoalPeriodItemUIState
import kotlinx.coroutines.flow.StateFlow

class GoalListStateHolder {

    private val _goalPeriodItemUIStateList = mutableStateListOf<GoalPeriodItemUIState>()
    val goalPeriodItemUIStateList: List<GoalPeriodItemUIState> = _goalPeriodItemUIStateList

    private val _selectedGoalPeriod = mutableStateOf("All")
    val selectedGoalPeriod: State<String> = _selectedGoalPeriod

    private val _successGoalList = mutableStateListOf<GoalItemUIState>()

    private val _failGoalList = mutableStateListOf<GoalItemUIState>()
    val failGoalList: List<GoalItemUIState> = _failGoalList

    private val _goalListToShow = mutableStateListOf<GoalItemUIState>()
    val goalListToShow: List<GoalItemUIState> = _goalListToShow

    suspend fun collectGoalListState(state: StateFlow<List<GoalItemUIState>>) {
        state.collect {
            _successGoalList.clear()
            _successGoalList.addAll(it.filter { goalItemUIState -> goalItemUIState.isSuccess })
            _failGoalList.clear()
            _failGoalList.addAll(it.filter { goalItemUIState -> !goalItemUIState.isSuccess })
            showGoalByPeriod(_selectedGoalPeriod.value)
        }
    }

    fun showGoalByPeriod(period: String) {
        _goalListToShow.clear()
        if (period == "All") {
            _goalListToShow.addAll(_successGoalList)
            return
        }
        _goalListToShow.addAll(_successGoalList.filter { it.period == period })
    }

    suspend fun collectGoalPeriodListState(state: StateFlow<List<GoalPeriodItemUIState>>) {
        state.collect { goalPeriodUIStateList ->
            _goalPeriodItemUIStateList.clear()
            _goalPeriodItemUIStateList.addAll(goalPeriodUIStateList)
        }
    }

    fun onSelectPeriod(period: String) {
        _selectedGoalPeriod.value = period
    }

    fun onClickGoal(goalId: Int) {

    }

}