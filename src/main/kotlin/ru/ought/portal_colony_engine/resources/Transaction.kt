package ru.ought.portal_colony_engine.resources

enum class TransactionState {
    CREATED, ACCEPTED, REJECTED
}

class Transaction(val bundle: Bundle) {
    private var _state = TransactionState.CREATED
    val state
        get() = _state

    fun accept() {
        _state = TransactionState.ACCEPTED
    }

    fun reject() {
        _state = TransactionState.REJECTED
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Transaction

        if (bundle != other.bundle) return false
        if (state != other.state) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bundle.hashCode()
        result = 31 * result + state.hashCode()
        return result
    }

    override fun toString(): String {
        return "Transaction(bundle=$bundle, state=$state)"
    }

}