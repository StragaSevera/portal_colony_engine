package ru.ought.portal_colony_engine.resources

import kotlin.reflect.KProperty

// "_private" parameter is needed for keeping the primary constructor private
// and having the ability to have a secondary constructor
// with the same type of parameters
// Resources is a proxy class that delegates the Map functionality to the dict parameter
class Bundle private constructor(
    private val dict: Map<ResourceLabel, Int>
) : Map<ResourceLabel, Int> by dict {
    companion object {
        // Adding the missing ResourceLabels to the dictionary
        operator fun invoke(dict: Map<ResourceLabel, Int>) {
            this(
                ResourceLabel.values().map {
                    it to dict.getOrDefault(it, 0)
                }.toMap()
            )
        }
    }

    constructor(vararg pairs: Pair<ResourceLabel, Int>) : this(pairs.toMap())

    constructor(
        food: Int = 0, water: Int = 0,
        wood: Int = 0, coal: Int = 0,
        stone: Int = 0, metal: Int = 0,
        oil: Int = 0, power: Int = 0
    ) : this(
        ResourceLabel.Food to food, ResourceLabel.Water to water,
        ResourceLabel.Wood to wood, ResourceLabel.Coal to coal,
        ResourceLabel.Stone to stone, ResourceLabel.Metal to metal,
        ResourceLabel.Oil to oil, ResourceLabel.Power to power
    )

    // Properties that are delegated to extract them from the dictionary
    val food: Int by DictProp
    val water: Int by DictProp
    val wood: Int by DictProp
    val coal: Int by DictProp
    val stone: Int by DictProp
    val metal: Int by DictProp
    val oil: Int by DictProp
    val power: Int by DictProp

    // Delegated property that extracts the values
    private object DictProp {
        operator fun getValue(bundle: Bundle, prop: KProperty<*>): Int =
            bundle[ResourceLabel.valueOf(prop.name.replaceFirstChar { it.uppercase() })]
    }

    override operator fun get(key: ResourceLabel): Int {
        return dict.getOrDefault(key, 0)
    }

    override fun toString(): String {
        return dict.toList().filter { (_, v) -> v != 0 }
            .joinToString(prefix = "Bundle(", postfix = ")") { (k, v) -> "$k = $v" }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Bundle) return false
        return all { (k, v) -> v == other[k] }
    }

    override fun hashCode(): Int {
        return dict.hashCode()
    }

    operator fun plus(arg: Bundle): Bundle {
        return Bundle(dict.map { (k, v) -> k to (v + arg[k]) }.toMap())
    }

    operator fun minus(arg: Bundle): Bundle {
        return Bundle(dict.map { (k, v) -> k to (v - arg[k]) }.toMap())
    }

    operator fun unaryMinus(): Bundle {
        return Bundle(dict.map { (k, v) -> k to -v }.toMap())
    }

    val isPositiveOrZero = this.values.all { it >= 0 }
    val isNegativeOrZero = this.values.all { it <= 0 }

    operator fun compareTo(arg: Bundle): Int {
        if (this == arg) return 0
        val diff = this - arg
        return when {
            diff.isPositiveOrZero -> 1
            diff.isNegativeOrZero -> -1
            else -> 0
        }
    }
}
