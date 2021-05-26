package ru.ought.portal_colony_engine.resources

import ch.tutteli.atrium.api.fluent.en_GB.toBe
import ch.tutteli.atrium.api.fluent.en_GB.toThrow
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

        it("adds resources") {
            val sut = Stash(Bundle(oil = 5, water = 5))
            sut.add(Bundle(water = 10))
            expect(sut.bundle).toBe(
                Bundle(water = 15, oil = 5)
            )
        }

        it("subtracts resources") {
            val sut = Stash(Bundle(oil = 5, water = 10))
            sut.remove(Bundle(water = 5))
            expect(sut.bundle).toBe(
                Bundle(water = 5, oil = 5)
            )
        }

        it("cannot subtract resources when not enough") {
            val sut = Stash(Bundle(oil = 5, water = 5))
            expect { sut.remove(Bundle(water = 10)) }.toThrow<IllegalArgumentException>()
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
})
