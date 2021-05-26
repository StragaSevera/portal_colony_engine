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


    fun add(arg: Bundle) {
        _bundle += arg
    }

    fun remove(arg: Bundle) {
        _bundle -= arg
    }

    fun hasResources(arg: Bundle): Boolean {
        require(arg.isPositiveOrZero)
        return bundle > arg
    }
}
