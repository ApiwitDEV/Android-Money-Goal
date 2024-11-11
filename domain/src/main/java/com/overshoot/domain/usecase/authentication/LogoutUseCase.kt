package com.overshoot.domain.usecase.authentication

import com.overshoot.data.repository.AuthenticationRepository
import com.overshoot.data.repository.CategoryRepository
import com.overshoot.data.repository.GoalRepository
import com.overshoot.data.repository.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class LogoutUseCase(
    private val coroutineScope: CoroutineScope,
    private val authenticationRepository: AuthenticationRepository,
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository,
    private val goalRepository: GoalRepository
) {

    suspend operator fun invoke() {
        listOf(
            coroutineScope.async {
                goalRepository.deleteAllGoal()
            },
            coroutineScope.async {
                categoryRepository.deleteAllCategories()
            },
            coroutineScope.async {
                transactionRepository.deleteAllTransactions()
            }
        ).awaitAll()
        coroutineScope.launch {
            authenticationRepository.logout()
        }
    }

}