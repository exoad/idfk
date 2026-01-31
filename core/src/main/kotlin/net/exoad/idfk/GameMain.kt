package net.exoad.idfk

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.async.KtxAsync
import ktx.graphics.use
import net.exoad.idfk.ecs.system.*
import net.exoad.idfk.levels.LevelManager
import net.exoad.idfk.ecs.attach

class GameMain : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()
        addScreen(Screen())
        setScreen<Screen>()
    }
}

class Screen : KtxScreen {
    private val batch = SpriteBatch()
    private val engine = Engine()

    private val camera = OrthographicCamera()
    private val viewport = FitViewport(1920f, 1080f, camera)

    init {
        viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
        val generator =
            FreeTypeFontGenerator(Gdx.files.internal("assets/Pizel.ttf"))
        generator.dispose()
        engine.attach {
            +HudSystem()
            +PlayerInputSystem()
            +MovementSystem()
            +HealthOrbSystem()
            +CollisionSystem()
            +FloatingTextSystem()
            +RenderSystem(
                batch,
                generator.generateFont(FreeTypeFontParameter().apply {
                    size = 16
                })
            )
            +PositionDisplaySystem()
        }
        LevelManager.load(engine)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        viewport.apply()
        clearScreen(red = 1f, green = 1f, blue = 1f, alpha = 1f)
        batch.projectionMatrix = camera.combined
        batch.use {
            engine.update(delta)
        }
    }

    override fun dispose() {
        engine.systems.forEach {
            if (it is RenderSystem) {
                it.dispose()
            }
        }
        batch.dispose()
    }
}
