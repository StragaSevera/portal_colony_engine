package ru.ought.portal_colony_engine.resources

enum class TransactionState {
    CREATED, ACCEPTED, REJECTED
}

interface ResourceEntity {
    fun getPredictedTransaction(): Transaction
    fun onPredictedTransactionProcessed(state: TransactionState)
    fun getRealTransaction()
}