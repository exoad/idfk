package net.exoad.idfk.world

import net.exoad.idfk.ecs.component.TileSetComponent
import net.exoad.idfk.util.SpriteRegistry
import net.exoad.idfk.util.SpriteRegistry.getSheetPathByKey
import net.exoad.idfk.util.WorldObjectRegistry

object WorldManager {
    private val worlds = mutableMapOf<String, World>()

    init {
        with(SpriteRegistry) {
            sheet("objects", "objects.png", 16, 16) {
                sprite("tree", gridX = 0, gridY = 0) {
                    offsetX = 1f
                    offsetY = 1f
                    width = 14f
                    height = 15f
                }
                sprite("signPosts", gridX = 1, gridY = 0) {
                    offsetX = 3f
                    offsetY = 5f
                    width = 10f
                    height = 10f
                }
            }
            with(WorldObjectRegistry) {
                registerFromSprite("tree", "tree", blocking = true)
                registerFromSprite("signPosts", "signPosts", blocking = true)
            }
            sheet("tiles", "tiles.png", frameWidth = 16, frameHeight = 16) { }
            sheet("player", "player.png", frameWidth = 16, frameHeight = 16) {
                sprite("player", 0, 0) {
                    offsetX = 4f
                    offsetY = 6f
                    width = 8f
                    height = 8f
                }
            }
            sheet("ui", "ui.png", frameWidth = 64, frameHeight = 64) {
                sprite("crosshair", 0, 0) { }
            }
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
