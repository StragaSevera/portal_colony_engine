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

    data class TransactionProcessingResult(val accepted: List<Transaction>, val rejected: List<Transaction>)

    fun processTransactions(transactions: List<Transaction>): TransactionProcessingResult {
        var unprocessed = transactions.toMutableList()
        val accepted = mutableListOf<Transaction>()
        var rejected = mutableListOf<Transaction>()

        do {
            for (transaction in unprocessed) {
                if (canAccept(transaction)) {
                    accepted.add(transaction)
                    _bundle += transaction.bundle
                } else {
                    rejected.add(transaction)
                }
            }

            if (rejected.isEmpty() || unprocessed.size == rejected.size) break

            unprocessed = rejected
            rejected = mutableListOf()
        } while (true)

        accepted.forEach { it.accept() }
        rejected.forEach { it.reject() }
        return TransactionProcessingResult(accepted, rejected)
    }

    private fun canAccept(transaction: Transaction): Boolean {
        return (bundle + transaction.bundle).isPositiveOrZero
    }
}
