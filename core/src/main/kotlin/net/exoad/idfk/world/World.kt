package net.exoad.idfk.world

import net.exoad.idfk.Shared
import net.exoad.idfk.Str
import net.exoad.idfk.ecs.component.TileMapComponent
import net.exoad.idfk.ecs.component.TileSetComponent
import net.exoad.idfk.util.TileGrid

data class World(
    val name: Str,
    val tileMapComponent: TileMapComponent,
    val tileSetComponent: TileSetComponent,
    val objectGrid: TileGrid,
    val spawnX: Float = Shared.World.SPAWN_X,
    val spawnY: Float = Shared.World.SPAWN_Y
)
