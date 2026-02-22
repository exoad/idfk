package net.exoad.idfk.world

import net.exoad.idfk.ecs.component.TileSetComponent
import net.exoad.idfk.util.TileMapLoader

object WorldManager {
    private val worlds = mutableMapOf<String, World>()

    init {
        registerBuiltInWorlds()
    }

    private fun registerBuiltInWorlds() {
        worlds["base"] = World(
            "BaseWorld",
            TileMapLoader.createTileMapFromString(
                WorldGenerator.generateWorldAsString(
                    14,
                    14,
                    intArrayOf(1, 2, 3, 4, 5)
                ),
                16,
                mapOf()
            ),
            TileSetComponent("tiles.png", 16, 4),
            160f,
            120f
        )
    }

    fun getWorld(name: String): World? {
        return worlds[name]
    }

    fun getBaseWorld(): World {
        return worlds["base"]!!
    }

    fun registerWorld(key: String, world: World) {
        worlds[key] = world
    }
}
