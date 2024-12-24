package com.overshoot.domain.usecase.transaction

import com.overshoot.domain.model.TransactionResult
import kotlinx.coroutines.flow.Flow

interface SubscribeTransactionUseCase {

    operator fun invoke(): Flow<List<TransactionResult>>

}