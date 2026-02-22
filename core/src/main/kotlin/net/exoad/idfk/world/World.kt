package net.exoad.idfk.world

import net.exoad.idfk.Shared
import net.exoad.idfk.ecs.component.TileMapComponent
import net.exoad.idfk.ecs.component.TileSetComponent

data class World(
    val name: String,
    val tileMapComponent: TileMapComponent,
    val tileSetComponent: TileSetComponent? = null,
    val spawnX: Float = Shared.World.SPAWN_X,
    val spawnY: Float = Shared.World.SPAWN_Y
)



