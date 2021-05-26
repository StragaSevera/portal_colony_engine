package ru.ought.portal_colony_engine.resources

class Stash(initialBundle: Bundle = Bundle()) {
    var bundle = initialBundle
        private set(arg) {
            require(arg.isPositiveOrZero)
            field = arg
        }

    fun addResources(arg: Bundle) {
        bundle += arg
    }

    fun removeResources(arg: Bundle) {
        bundle -= arg
    }

    fun hasResources(arg: Bundle): Boolean {
        require(arg.isPositiveOrZero)
        return bundle > arg
    }
}
