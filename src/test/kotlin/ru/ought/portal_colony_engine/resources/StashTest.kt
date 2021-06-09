package ru.ought.portal_colony_engine.resources

import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.expect
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.mockk

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
        val entity = mockk<ResourceEntity>()

        context("processTransactions") {
            it("accepts correct transactions") {
                val sut = Stash(Bundle(oil = 5, water = 5))
                val transactions = listOf(
                    Transaction(Bundle(oil = 2), entity),
                    Transaction(Bundle(water = -5), entity)
                )

                val result = sut.processTransactions(transactions)

                expect(result.accepted).toBe(transactions)
                expect(result.rejected).isEmpty()
                expect(result.combinedBundle).toBe(Bundle(oil = 2, water = -5))
            }

            it("rejects incorrect transactions") {
                val sut = Stash(Bundle(oil = 5, water = 5))
                val transactions = listOf(
                    Transaction(Bundle(oil = -7), entity),
                    Transaction(Bundle(wood = -1), entity)
                )

                val result = sut.processTransactions(transactions)

                expect(result.accepted).isEmpty()
                expect(result.rejected).toBe(transactions)
                expect(result.combinedBundle).toBe(Bundle())
            }

            it("accepts and rejects transactions") {
                val sut = Stash(Bundle(oil = 5, water = 5))
                val transactions = listOf(
                    Transaction(Bundle(oil = -2), entity),
                    Transaction(Bundle(wood = -1), entity)
                )

                val result = sut.processTransactions(transactions)

                expect(result.accepted).containsExactly(transactions.first())
                expect(result.rejected).containsExactly(transactions.last())
                expect(result.combinedBundle).toBe(Bundle(oil = -2))
            }

            it("does not change contents of the stash") {
                val sut = Stash(Bundle(oil = 5, water = 5))
                val transactions = listOf(
                    Transaction(Bundle(oil = -2), entity),
                    Transaction(Bundle(wood = -1), entity)
                )

                sut.processTransactions(transactions)

                expect(sut.bundle).toBe(Bundle(oil = 5, water = 5))
            }

            it("accepts transactions if balance is fixed by later transaction") {
                val sut = Stash(Bundle(oil = 5, water = 5))
                val transactions = listOf(
                    Transaction(Bundle(oil = -2), entity),
                    Transaction(Bundle(wood = -1), entity),
                    Transaction(Bundle(wood = 2), entity),
                )

                val result = sut.processTransactions(transactions)

                expect(result.accepted).contains.inAnyOrder.only.elementsOf(transactions)
                expect(result.rejected).isEmpty()
                expect(result.combinedBundle).toBe(Bundle(oil = -2, wood = 1))
            }

            it("handles a complex example") {
                val sut = Stash(Bundle(oil = 5, water = 5))
                val transactions = listOf(
                    Transaction(Bundle(oil = -2), entity),
                    Transaction(Bundle(wood = -1), entity),
                    Transaction(Bundle(wood = 2), entity),
                    Transaction(Bundle(wood = -3), entity),
                    Transaction(Bundle(food = 5, coal = -3), entity),
                    Transaction(Bundle(food = -5, coal = 3), entity),
                    Transaction(Bundle(metal = -2), entity),
                    Transaction(Bundle(metal = -1), entity),
                    Transaction(Bundle(metal = 1), entity),
                )
                val acceptedTransactions = transactions.slice(0..2) + transactions.slice(7..8)
                val rejectedTransactions = transactions.slice(3..6)

                val result = sut.processTransactions(transactions)

                expect(result.accepted).contains.inAnyOrder.only.elementsOf(acceptedTransactions)
                expect(result.rejected).contains.inAnyOrder.only.elementsOf(rejectedTransactions)
                expect(result.combinedBundle).toBe(Bundle(oil = -2, wood = 1))
            }
        }

        context("applyTransactions") {
            it("changes the stash in a complex example") {
                val sut = Stash(Bundle(oil = 5, water = 5))
                val transactions = listOf(
                    Transaction(Bundle(oil = -2), entity),
                    Transaction(Bundle(wood = -1), entity),
                    Transaction(Bundle(wood = 2), entity),
                    Transaction(Bundle(wood = -3), entity),
                    Transaction(Bundle(food = 5, coal = -3), entity),
                    Transaction(Bundle(food = -5, coal = 3), entity),
                    Transaction(Bundle(metal = -2), entity),
                    Transaction(Bundle(metal = -1), entity),
                    Transaction(Bundle(metal = 1), entity),
                )

                sut.applyTransactions(transactions)

                expect(sut.bundle).toBe(Bundle(oil = 3, water = 5, wood = 1))
            }
        }
    }
})
