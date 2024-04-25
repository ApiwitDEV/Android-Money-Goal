package com.overshoot.moneygoal.component.home.stateholder.viewmodel

import com.overshoot.domain.GetGoalUseCase
import com.overshoot.domain.AddGoalUseCase
import com.overshoot.moneygoal.BaseViewModel
import com.overshoot.moneygoal.common.UIState
import com.overshoot.moneygoal.component.home.uistatemodel.GoalItemUIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GoalViewModel(
    private val getGoalUseCase: GetGoalUseCase,
    private val addGoalUseCase: AddGoalUseCase
): BaseViewModel() {

    init {
        println()
    }

    private var executeCount = 0

    private val _addGoalState = MutableStateFlow(UIState.NO_STATE)
    val addGoalState: StateFlow<UIState> = _addGoalState

    private val _allGoal = MutableStateFlow<MutableList<GoalItemUIState>>(mutableListOf())
    val allGoal: StateFlow<List<GoalItemUIState>> = _allGoal

    fun clearExecuteCount() {
        executeCount = 0
    }

    fun addGoal() {
        _addGoalState.value = UIState.LOADING
        executeUseCase(
            mapToUIState = {

            },
            action = {
                delay(2000)
                addGoalUseCase.addGoal()
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
                                objective = "limit",
                                target = 1000.0
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

}