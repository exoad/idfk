package net.exoad.idfk

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.GL30
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import net.exoad.idfk.world.WorldManager
import org.tinylog.kotlin.Logger

class GameMain : KtxGame<KtxScreen>() {
    override fun create() {
        val gl20Available = Gdx.gl20 != null
        val gl30Available = Gdx.gl30 != null
        Logger.info("OpenGL: GL20 available=$gl20Available, GL30 available=$gl30Available")
        try {
            try {
                if (gl30Available && Gdx.gl30 != null) {
                    Gdx.gl30!!.glEnable(GL30.GL_BLEND)
                    Gdx.gl30!!.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA)
                } else {
                    Gdx.gl.glEnable(GL20.GL_BLEND)
                    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
                }
            } catch (e: Throwable) {
            }
            try {
                Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
            } catch (_: Throwable) {
            }
            try {
                Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST)
            } catch (_: Throwable) {
            }
        } catch (_: Throwable) {
        }
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
