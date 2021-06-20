package ru.ought.portal_colony_engine.resources

import ru.ought.portal_colony_engine.entities.ResourceEntity

data class Transaction(val bundle: Bundle, val entity: ResourceEntity)
enum class TransactionState {
    CREATED, ACCEPTED, REJECTED
}