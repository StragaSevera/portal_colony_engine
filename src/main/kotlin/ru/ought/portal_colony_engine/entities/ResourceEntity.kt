package ru.ought.portal_colony_engine.entities

import ru.ought.portal_colony_engine.resources.Transaction
import ru.ought.portal_colony_engine.resources.TransactionState

interface ResourceEntity {
    fun getPredictedTransaction(game: Game): Transaction
    fun onPredictedTransactionProcessed(state: TransactionState)
    fun getRealTransaction()
}