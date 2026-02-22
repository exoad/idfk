package net.exoad.idfk.world

import net.exoad.idfk.ecs.component.TileSetComponent
import net.exoad.idfk.util.TileMapLoader

object WorldManager {
    private val worlds = mutableMapOf<String, World>()

    init {
        worlds["base"] = World(
            name = "BaseWorld",
            tileMapComponent = TileMapLoader.createTileMapFromString(
                WorldGenerator.generateWorldAsString(
                    width = 20,
                    height = 20,
                    textureIndices = intArrayOf(1, 2, 3, 4, 5)
                ),
                tileSize = 16,
            ),
            tileSetComponent = TileSetComponent("tiles.png", 16),
            spawnX = 160f,
            spawnY = 160f,
        )
    }

    operator fun get(name: String): World {
        return worlds[name] ?: throw IllegalArgumentException("World '$name' not found.")
    }

    operator fun set(key: String, world: World) {
        worlds[key] = world
    }
}
