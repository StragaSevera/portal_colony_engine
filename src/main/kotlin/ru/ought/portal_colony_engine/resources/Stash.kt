package ru.ought.portal_colony_engine.resources

class Stash(initialBundle: Bundle = Bundle()) {
    private var _bundle = initialBundle
        set(arg) {
            require(arg.isPositiveOrZero)
            field = arg
        }

    // Isolated so IntelliJ wouls stop incorrectly reporting it as mutable.
    // Relevant issue: https://youtrack.jetbrains.com/issue/KT-35409
    val bundle
        get() = _bundle


    fun hasResources(arg: Bundle): Boolean {
        require(arg.isPositiveOrZero)
        return bundle > arg
    }

    data class TransactionProcessingResult(val combinedBundle: Bundle, val accepted: List<Transaction>, val rejected: List<Transaction>)

    fun processTransactions(transactions: List<Transaction>): TransactionProcessingResult {
        var nextBundle = bundle
        var unprocessed = transactions.toMutableList()
        val accepted = mutableListOf<Transaction>()
        var rejected = mutableListOf<Transaction>()

        do {
            for (transaction in unprocessed) {
                if (canAccept(nextBundle, transaction)) {
                    accepted.add(transaction)
                    nextBundle += transaction.bundle
                } else {
                    rejected.add(transaction)
                }
            }

            if (rejected.isEmpty() || unprocessed.size == rejected.size) break

            unprocessed = rejected
            rejected = mutableListOf()
        } while (true)

        return TransactionProcessingResult(nextBundle - bundle, accepted, rejected)
    }

    fun applyTransactions(transactions: List<Transaction>) {
        val data = processTransactions(transactions)
        _bundle += data.combinedBundle
    }

    private fun canAccept(nextBundle: Bundle, transaction: Transaction): Boolean {
        return (nextBundle + transaction.bundle).isPositiveOrZero
    }
}
