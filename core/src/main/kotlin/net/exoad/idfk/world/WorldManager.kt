package net.exoad.idfk.world

import net.exoad.idfk.ecs.component.TileSetComponent
import net.exoad.idfk.util.GameObject
import net.exoad.idfk.util.SpriteRegistry
import net.exoad.idfk.util.TileGrid
import net.exoad.idfk.util.TileMapLoader

object WorldManager {
    private val worlds = mutableMapOf<String, World>()

    init {
        with(SpriteRegistry) {
            defineSheet("items", "items.png", 16, 16)
            defineSpriteByGrid("tree", "items", 0, 0)
            defineSheet("tiles", "tiles.png", 16, 16)
            defineSheet("player", "player.png", 16, 16)
            val width = 20
            val height = 20
            val tileMap = TileMapLoader.createTileMapFromString(
                WorldGenerator.generateWorldAsString(
                    width = width,
                    height = height,
                    textureIndices = intArrayOf(1, 2, 3, 4, 5)
                ),
                tileSize = 16,
            )

            val grid = TileGrid(width, height)
            val tree = GameObject(id = 1, type = "tree")
            grid.placeObject(10, 10, tree)
            worlds["base"] = World(
                name = "BaseWorld",
                tileMapComponent = tileMap,
                tileSetComponent = TileSetComponent(getSheetPathByKey("tiles") ?: "tiles.png", 16),
                objectGrid = grid,
                spawnX = 160f,
                spawnY = 160f,
            )
        }
    }

    operator fun get(name: String): World {
        return worlds[name] ?: throw IllegalArgumentException("World '$name' not found.")
    }

    operator fun set(key: String, world: World) {
        worlds[key] = world
    }
}
