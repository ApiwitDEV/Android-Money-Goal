package com.overshoot.moneygoal.component.home.stateholder

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import com.overshoot.moneygoal.component.home.uistatemodel.GoalItemUIState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.last

class SuccessGoalListStateHolder {

    private val _successGoalList = mutableStateListOf<GoalItemUIState>()
    val successGoalList: List<GoalItemUIState> = _successGoalList

    private val _failGoalList = mutableStateListOf<GoalItemUIState>()
    val failGoalList: List<GoalItemUIState> = _failGoalList

    suspend fun collectGoalListState(state: StateFlow<List<GoalItemUIState>>) {
        state.collect {
            _successGoalList.clear()
            _successGoalList.addAll(it.filter { goalItemUIState -> goalItemUIState.isSuccess })
            _failGoalList.clear()
            _failGoalList.addAll(it.filter { goalItemUIState -> !goalItemUIState.isSuccess })
        }
    }

}