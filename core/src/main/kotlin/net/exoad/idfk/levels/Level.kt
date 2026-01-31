package net.exoad.idfk.levels

import com.badlogic.ashley.core.Engine

interface Level {
    fun load(engine: Engine)
}

object LevelManager {
    const val DEFAULT_MAP_ID = "default"

    private val levels = mapOf<String, Level>(DEFAULT_MAP_ID to Level0())

    fun load(engine: Engine, levelId: String = DEFAULT_MAP_ID) {
        levels[levelId]?.load(engine) ?: levels[DEFAULT_MAP_ID]?.load(engine)
    }
}
