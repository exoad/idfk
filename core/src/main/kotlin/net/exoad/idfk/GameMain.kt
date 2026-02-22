package net.exoad.idfk

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import net.exoad.idfk.world.WorldManager
import org.tinylog.kotlin.Logger

class GameMain : KtxGame<KtxScreen>() {
    override fun create() {
        Logger.info(
            "WORLD:\n${
                buildString {
                    val world = WorldManager["base"]
                    for (y in 0 until world.tileMapComponent.height) {
                        for (x in 0 until world.tileMapComponent.width) {
                            append(world.tileMapComponent.tiles[y][x]).append(" ")
                        }
                        appendLine()
                    }
                }
            }"
        )
        KtxAsync.initiate()
        addScreen(Screen())
        setScreen<Screen>()
    }
}

