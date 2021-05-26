package ru.ought.portal_colony_engine.resources

import ch.tutteli.atrium.api.fluent.en_GB.notToBe
import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.verbs.expect
import io.kotest.core.spec.style.DescribeSpec


class BundleTest : DescribeSpec({
    describe("basic tests") {
        context("with initialization and access") {
            it("has multiple resource types") {
                val sut = Bundle()
                expect(sut.size).toBe(8)
            }

            it("can be accessed by name") {
                val sut = Bundle(food = 10)
                expect(sut.food).toBe(10)
            }

            it("can be accessed by type") {
                val sut = Bundle(ResourceLabel.Food to 10)
                expect(sut[ResourceLabel.Food]).toBe(10)
            }

            it("has default values") {
                val sut = Bundle(food = 10)
                expect(sut.coal).toBe(0)
            }
        }

        context("with operations") {
            it("can be equal to same resource") {
                val sut = Bundle(food = 10)
                expect(sut).toBe(Bundle(food = 10))
            }

            it("is not equal to different resource") {
                val sut = Bundle(food = 10)
                expect(sut).notToBe(Bundle(food = 5))
            }

            it("has equal hashcode to same resource") {
                val sut = Bundle(food = 10)
                expect(sut.hashCode()).toBe(Bundle(food = 10).hashCode())
            }

            it("has different hashcode to different resource") {
                val sut = Bundle(food = 10)
                expect(sut.hashCode()).notToBe(Bundle(food = 5).hashCode())
            }

            it("can be added") {
                val first = Bundle(food = 10, power = 5)
                val second = Bundle(wood = 8, power = 4)
                val sut = first + second
                expect(sut).toBe(
                    Bundle(food = 10, wood = 8, power = 9)
                )
            }

            it("can be subtracted") {
                val first = Bundle(food = 10, power = 5)
                val second = Bundle(wood = 8, power = 4)
                val sut = first - second
                expect(sut).toBe(
                    Bundle(food = 10, wood = -8, power = 1)
                )
            }

            it("can be negated") {
                val sut = -Bundle(food = 10, power = -5)
                expect(sut).toBe(
                    Bundle(food = -10, power = 5)
                )
            }

            it("can be partially compared") {
                val first = Bundle(food = 10, power = 5)
                val second = Bundle(food = 8, power = 5)
                expect(first > second).toBe(true)
                expect(first < second).toBe(false)
                expect(second < first).toBe(true)
                expect(second > first).toBe(false)
            }

            it("cannot be compared when incomparable") {
                val first = Bundle(food = 10, coal = 5)
                val second = Bundle(food = 8, power = 5)
                expect(first > second).toBe(false)
                expect(second < first).toBe(false)
                expect(first == second).toBe(false)
            }

            context("with weak positivity/negativity") {
                it("can be checked for weak positivity when positive") {
                    val sut = Bundle(food = 10, power = 5)
                    expect(sut.isPositiveOrZero).toBe(true)
                }

                it("can be checked for weak positivity when zero") {
                    val sut = Bundle()
                    expect(sut.isPositiveOrZero).toBe(true)
                }

                it("can be checked for weak positivity when partially negative") {
                    val sut = Bundle(food = 10, power = -5)
                    expect(sut.isPositiveOrZero).toBe(false)
                }

                it("can be checked for weak negativity when negative") {
                    val sut = Bundle(food = -10, power = -5)
                    expect(sut.isNegativeOrZero).toBe(true)
                }

                it("can be checked for weak negativity when zero") {
                    val sut = Bundle()
                    expect(sut.isNegativeOrZero).toBe(true)
                }

                it("can be checked for weak negativity when partially positive") {
                    val sut = Bundle(food = 10, power = -5)
                    expect(sut.isNegativeOrZero).toBe(false)
                }

            }
        }
    }
})
