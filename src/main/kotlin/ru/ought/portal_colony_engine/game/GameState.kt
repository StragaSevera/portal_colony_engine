package ru.ought.portal_colony_engine.game

import ru.ought.portal_colony_engine.entities.ResourceEntity
import ru.ought.portal_colony_engine.resources.Stash
import ru.ought.portal_colony_engine.timeline.getSeason

class GameState {
    val stash = Stash()

    private var _currentDay = 0
    val currentDay
        get() = _currentDay

    val currentSeason
        get() = currentDay.getSeason()

    private val _entities = mutableListOf<ResourceEntity>()
    val entities: List<ResourceEntity>
        get() = _entities

    fun addEntity(entity: ResourceEntity) {
        _entities.add(entity)
    }
}