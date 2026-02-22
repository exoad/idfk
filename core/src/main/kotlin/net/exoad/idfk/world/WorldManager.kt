package net.exoad.idfk.world

import net.exoad.idfk.ecs.component.TileSetComponent
import net.exoad.idfk.util.TileMapLoader

object WorldManager {
    private val worlds = mutableMapOf<String, World>()

    init {
        registerBuiltInWorlds()
    }

    private fun registerBuiltInWorlds() {
        val baseWorldTilemap = TileMapLoader.createTileMapFromString(
            """
            0 1 2 3 0 1 2 3 0 1 2 3 0 1 2 3 0 1 2 3
            4 5 6 7 4 5 6 7 4 5 6 7 4 5 6 7 4 5 6 7
            8 9 10 11 8 9 10 11 8 9 10 11 8 9 10 11 8 9 10 11
            12 13 14 15 12 13 14 15 12 13 14 15 12 13 14 15 12 13 14 15
            0 1 2 3 0 1 2 3 0 1 2 3 0 1 2 3 0 1 2 3
            4 5 6 7 4 5 6 7 4 5 6 7 4 5 6 7 4 5 6 7
            8 9 10 11 8 9 10 11 8 9 10 11 8 9 10 11 8 9 10 11
            12 13 14 15 12 13 14 15 12 13 14 15 12 13 14 15 12 13 14 15
            0 1 2 3 0 1 2 3 0 1 2 3 0 1 2 3 0 1 2 3
            4 5 6 7 4 5 6 7 4 5 6 7 4 5 6 7 4 5 6 7
            8 9 10 11 8 9 10 11 8 9 10 11 8 9 10 11 8 9 10 11
            12 13 14 15 12 13 14 15 12 13 14 15 12 13 14 15 12 13 14 15
            0 1 2 3 0 1 2 3 0 1 2 3 0 1 2 3 0 1 2 3
            4 5 6 7 4 5 6 7 4 5 6 7 4 5 6 7 4 5 6 7
            8 9 10 11 8 9 10 11 8 9 10 11 8 9 10 11 8 9 10 11
            """.trimIndent(),
            tileSize = 16,
            tileTextureMap = mapOf()
        )

        val baseWorldTileset = TileSetComponent(
            tilesetPath = "tiles.png",
            tileSize = 16,
            tilesPerRow = 4
        )

        worlds["base"] = World(
            name = "BaseWorld",
            tileMapComponent = baseWorldTilemap,
            tileSetComponent = baseWorldTileset,
            spawnX = 160f,
            spawnY = 120f
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
