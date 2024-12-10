package com.overshoot.moneygoalapp.component.home.stateholder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overshoot.data.datasource.onFailure
import com.overshoot.data.datasource.onSuccess
import com.overshoot.domain.usecase.goal.GetGoalUseCase
import com.overshoot.domain.usecase.goal.AddGoalUseCase
import com.overshoot.moneygoalapp.common.Period
import com.overshoot.moneygoalapp.component.home.uistatemodel.AddGoalResultUIState
import com.overshoot.moneygoalapp.component.home.uistatemodel.GoalItemUIState
import com.overshoot.moneygoalapp.component.home.uistatemodel.GoalPeriodItemUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeGoalDetailViewModel(
    private val getGoalUseCase: GetGoalUseCase,
    private val addGoalUseCase: AddGoalUseCase
): ViewModel() {

    private val _addGoalLoading = MutableStateFlow(false)
    val addGoalLoading = _addGoalLoading.asStateFlow()

    private val _addGoalResult = MutableStateFlow<AddGoalResultUIState?>(null)
    val addGoalResult = _addGoalResult.asStateFlow()

    private val _allGoal = MutableStateFlow<List<GoalItemUIState>>(mutableListOf())
    val allGoal: StateFlow<List<GoalItemUIState>> = _allGoal

    private val _goalPeriodList = MutableStateFlow<List<GoalPeriodItemUIState>>(listOf())
    val goalPeriodList: StateFlow<List<GoalPeriodItemUIState>> = _goalPeriodList

    fun addGoal(
        goalName: String,
        goalObjective: String,
        period: String,
        targetValue: Double
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _addGoalLoading.value = true
            addGoalUseCase.addGoal(
                goalName = goalName,
                goalObjective = goalObjective,
                period = period,
                targetValue = targetValue
            )
                .onSuccess {
                    _addGoalLoading.value = false
                    _addGoalResult.value = AddGoalResultUIState.SUCCESS
                }
                .onFailure {
                    _addGoalLoading.value = false
                    _addGoalResult.value = AddGoalResultUIState.FAILURE
                }
        }
    }

    fun observeAllGoal() {
        viewModelScope.launch(Dispatchers.IO) {
            getGoalUseCase.getAllGoal()
                .map {
                    mutableListOf<GoalItemUIState>().apply {
                        it.forEach {
                            this.add(
                                GoalItemUIState(
                                    id = it.id,
                                    name = it.name?:"",
                                    isSuccess = true,
                                    objective = it.objective?:"",
                                    target = it.target?:0.0,
                                    period = it.period?:""
                                )
                            )
                        }
                    }
                }
                .collect {
                    _allGoal.value = it
                }
        }
    }

    fun observeGoalPeriodList() {
        viewModelScope.launch(Dispatchers.IO) {
            getGoalUseCase.getAllGoal()
                .map { goalList ->
                    val goalPeriodList = mutableListOf<GoalPeriodItemUIState>()
                    val x = goalList.count {
                        it.period == Period.DAILY.value
                    }
                    val y = goalList.count {
                        it.period == Period.WEEKLY.value
                    }
                    val z = goalList.count {
                        it.period == Period.MONTHLY.value
                    }
                    goalPeriodList.add(
                        GoalPeriodItemUIState(
                            period = "All",
                            count = x+y+z
                        )
                    )
                    if (x != 0) {
                        goalPeriodList.add(
                            GoalPeriodItemUIState(
                                period = Period.DAILY.value,
                                count = x
                            )
                        )
                    }
                    if (y != 0) {
                        goalPeriodList.add(
                            GoalPeriodItemUIState(
                                period = Period.WEEKLY.value,
                                count = y
                            )
                        )
                    }
                    if (z != 0) {
                        goalPeriodList.add(
                            GoalPeriodItemUIState(
                                period = Period.MONTHLY.value,
                                count = z
                            )
                        )
                    }
                    goalPeriodList
                }
                .collect {
                    _goalPeriodList.value = it
                }
        }
    }

}