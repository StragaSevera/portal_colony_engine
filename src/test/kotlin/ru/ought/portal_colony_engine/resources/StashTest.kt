package ru.ought.portal_colony_engine.resources

import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.expect
import io.kotest.core.spec.style.DescribeSpec

class StashTest : DescribeSpec({
    describe("basic tests") {
        it("has empty bundle by default") {
            val sut = Stash()
            expect(sut.bundle).toBe(Bundle())
        }

        it("can be initialized with bundle") {
            val sut = Stash(Bundle(oil = 5))
            expect(sut.bundle).toBe(Bundle(oil = 5))
        }

        it("can check if has resources when enough") {
            val sut = Stash(Bundle(oil = 5, water = 5))
            expect(sut.hasResources(Bundle(oil = 5, water = 2))).toBe(true)
        }

        it("can check if has resources when not enough") {
            val sut = Stash(Bundle(oil = 5, water = 5))
            expect(sut.hasResources(Bundle(oil = 5, coal = 2))).toBe(false)
        }

        it("cannot check if has resources when not positive") {
            val sut = Stash(Bundle(oil = 5, water = 5))
            expect { sut.hasResources(Bundle(oil = 5, water = -2)) }.toThrow<IllegalArgumentException>()
        }
    }

    describe("transactions") {
        it("accepts correct transactions") {
            val sut = Stash(Bundle(oil = 5, water = 5))
            val transactions = listOf(
                Transaction(Bundle(oil = 2)),
                Transaction(Bundle(water = -5))
            )

            val result = sut.processTransactions(transactions)

            expect(result.accepted).toBe(transactions)
            expect(result.rejected).isEmpty()
            expect(transactions).all { its { state }.toBe(TransactionState.ACCEPTED) }
        }

        it("rejects incorrect transactions") {
            val sut = Stash(Bundle(oil = 5, water = 5))
            val transactions = listOf(
                Transaction(Bundle(oil = -7)),
                Transaction(Bundle(wood = -1))
            )

            val result = sut.processTransactions(transactions)

            expect(result.accepted).isEmpty()
            expect(result.rejected).toBe(transactions)
            expect(transactions).all { its { state }.toBe(TransactionState.REJECTED) }
        }

        it("accepts and rejects transactions") {
            val sut = Stash(Bundle(oil = 5, water = 5))
            val transactions = listOf(
                Transaction(Bundle(oil = -2)),
                Transaction(Bundle(wood = -1))
            )

            val result = sut.processTransactions(transactions)

            expect(result.accepted).containsExactly(transactions.first())
            expect(result.rejected).containsExactly(transactions.last())

            expect(transactions.first().state).toBe(TransactionState.ACCEPTED)
            expect(transactions.last().state).toBe(TransactionState.REJECTED)
        }

        it("changes contents of the stash") {
            val sut = Stash(Bundle(oil = 5, water = 5))
            val transactions = listOf(
                Transaction(Bundle(oil = -2)),
                Transaction(Bundle(wood = -1))
            )

            sut.processTransactions(transactions)

            expect(sut.bundle).toBe(Bundle(oil = 3, water = 5))
        }

        it("accepts transactions if balance is fixed by later transaction") {
            val sut = Stash(Bundle(oil = 5, water = 5))
            val transactions = listOf(
                Transaction(Bundle(oil = -2)),
                Transaction(Bundle(wood = -1)),
                Transaction(Bundle(wood = 2)),
            )

            val result = sut.processTransactions(transactions)

            expect(result.accepted).contains.inAnyOrder.only.elementsOf(transactions)
            expect(result.rejected).isEmpty()
            expect(transactions).all { its { state }.toBe(TransactionState.ACCEPTED) }

            expect(sut.bundle).toBe(Bundle(oil = 3, water = 5, wood = 1))
        }

        it("handles a complex example") {
            val sut = Stash(Bundle(oil = 5, water = 5))
            val transactions = listOf(
                Transaction(Bundle(oil = -2)),
                Transaction(Bundle(wood = -1)),
                Transaction(Bundle(wood = 2)),
                Transaction(Bundle(wood = -3)),
                Transaction(Bundle(food = 5, coal = -3)),
                Transaction(Bundle(food = -5, coal = 3)),
            )
            val acceptedTransactions = transactions.slice(0..2)
            val rejectedTransactions = transactions.slice(3..5)

            val result = sut.processTransactions(transactions)

            expect(result.accepted).contains.inAnyOrder.only.elementsOf(acceptedTransactions)
            expect(result.rejected).contains.inAnyOrder.only.elementsOf(rejectedTransactions)

            expect(acceptedTransactions).all { its { state }.toBe(TransactionState.ACCEPTED) }
            expect(rejectedTransactions).all { its { state }.toBe(TransactionState.REJECTED) }

            expect(sut.bundle).toBe(Bundle(oil = 3, water = 5, wood = 1))
        }
    }
})
