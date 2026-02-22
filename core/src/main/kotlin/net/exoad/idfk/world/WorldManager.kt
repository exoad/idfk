package net.exoad.idfk.world

import net.exoad.idfk.ecs.component.TileSetComponent
import net.exoad.idfk.util.SpriteRegistry
import net.exoad.idfk.util.SpriteRegistry.getSheetPathByKey
import net.exoad.idfk.util.WorldObjectRegistry

object WorldManager {
    private val worlds = mutableMapOf<String, World>()

    init {
        with(SpriteRegistry) {
            sheet("items", "items.png", 16, 16) {
                sprite("tree", 0, 0) {
                    offsetX = 1f
                    offsetY = 1f
                    width = 14f
                    height = 15f
                }
            }
            sheet("tiles", "tiles.png", 16, 16) { }
            sheet("player", "player.png", 16, 16) {
                sprite("player", 0, 0) {
                    offsetX = 4f
                    offsetY = 1f
                    width = 8f
                    height = 14f
                }
            }
        }
        with(WorldObjectRegistry) {
            registerConverted(
                "tree",
                "tree",
                spriteHeight = 16f,
                offsetX = 0f,
                offsetY = 0f,
                width = 16f,
                height = 16f,
                blocking = true
            )
        }
        worlds["base"] = WorldGenerator.generateWorld(
            width = 32,
            height = 32,
            textureIndices = intArrayOf(1, 2, 3, 4, 5),
            spawnX = 0f,
            spawnY = 0f,
            tileSetComponent = TileSetComponent(getSheetPathByKey("tiles"), 16),
            name = "base"
        )
    }

    operator fun get(name: String): World {
        return worlds[name] ?: throw IllegalArgumentException("World '$name' not found.")
    }

    operator fun set(key: String, world: World) {
        worlds[key] = world
    }
}
