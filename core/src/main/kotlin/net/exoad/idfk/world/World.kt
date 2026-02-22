package net.exoad.idfk.world

import net.exoad.idfk.ecs.component.TileMapComponent
import net.exoad.idfk.ecs.component.TileSetComponent

data class World(
    val name: String,
    val tileMapComponent: TileMapComponent,
    val tileSetComponent: TileSetComponent? = null,
    val spawnX: Float = 0f,
    val spawnY: Float = 0f
)



