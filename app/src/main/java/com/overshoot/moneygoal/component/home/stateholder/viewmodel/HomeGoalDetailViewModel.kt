package com.overshoot.moneygoal.component.home.stateholder.viewmodel

import com.overshoot.domain.GetGoalUseCase
import com.overshoot.domain.AddGoalUseCase
import com.overshoot.moneygoal.BaseViewModel
import com.overshoot.moneygoal.common.Period
import com.overshoot.moneygoal.common.UIState
import com.overshoot.moneygoal.component.home.uistatemodel.GoalItemUIState
import com.overshoot.moneygoal.component.home.uistatemodel.GoalPeriodItemUIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeGoalDetailViewModel(
    private val getGoalUseCase: GetGoalUseCase,
    private val addGoalUseCase: AddGoalUseCase
): BaseViewModel() {

    init {
        println()
    }

    private var executeCount = 0

    private val _addGoalState = MutableStateFlow(UIState.NO_STATE)
    val addGoalState: StateFlow<UIState> = _addGoalState

    private val _allGoal = MutableStateFlow<List<GoalItemUIState>>(mutableListOf())
    val allGoal: StateFlow<List<GoalItemUIState>> = _allGoal

    private val _goalPeriodList = MutableStateFlow<List<GoalPeriodItemUIState>>(listOf())
    val goalPeriodList: StateFlow<List<GoalPeriodItemUIState>> = _goalPeriodList

    fun clearExecuteCount() {
        executeCount = 0
    }

    fun addGoal(
        goalName: String,
        goalObjective: String,
        period: String,
        targetValue: Double
    ) {
        _addGoalState.value = UIState.LOADING
        executeUseCase(
            mapToUIState = {

            },
            action = {
                delay(500)
                addGoalUseCase.addGoal(
                    goalName = goalName,
                    goalObjective = goalObjective,
                    period = period,
                    targetValue = targetValue
                )
            },
            onSuccess = {
                _addGoalState.value = UIState.SUCCESS
            },
            onFailure = {
                it
                _addGoalState.value = UIState.FAILURE
            },
            onConnectingNotAvailable = {
                _addGoalState.value = UIState.NO_INTERNET
            }
        )
    }

//    fun getAllGoal() {
//        if (executeCount == 0) {
//            executeUseCase(
//                action = {
//                    getGoalUseCase.getAllGoal()
//                },
//                mapToUIState = {
//                    mutableListOf<GoalItemUIState>().apply {
//                        it.forEach {
//                            this.add(
//                                GoalItemUIState(
//                                    id = it.id,
//                                    name = it.name?:"",
//                                    isSuccess = true
//                                )
//                            )
//                        }
//                    }
//                },
//                onSuccess = {
//                    _allGoal.value = it
//                    executeCount += 1
//                },
//                onFailure = {
//
//                },
//                onConnectingNotAvailable = {
//
//                }
//            )
//        }
//    }

    fun observeAllGoal() {
        observeStreamingData(
            action = {
                getGoalUseCase.getAllGoal()
            },
            mapToUIState = {
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
            },
            onDataReceived = {
                _allGoal.value = it
            }
        )
    }

    fun observeGoalPeriodList() {
        observeStreamingData(
            action = {
                getGoalUseCase.getAllGoal()
            },
            mapToUIState = { goalList ->
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
            },
            onDataReceived = {
                _goalPeriodList.value = it
            }
        )
    }

}