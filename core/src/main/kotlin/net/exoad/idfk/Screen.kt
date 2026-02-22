package net.exoad.idfk

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.math.Vector2
import ktx.app.KtxScreen
import ktx.app.clearScreen
import net.exoad.idfk.ecs.system.*
import net.exoad.idfk.factories.PlayerFactory
import net.exoad.idfk.factories.TileMapFactory
import net.exoad.idfk.world.WorldManager

class Screen : KtxScreen {
    private val batch = SpriteBatch()
    private val engine = Engine()
    private val camera = OrthographicCamera()

    init {
        with(camera) {
            setToOrtho(
                false,
                Gdx.graphics.width.toFloat() / Shared.VISUAL_SCALE,
                Gdx.graphics.height.toFloat() / Shared.VISUAL_SCALE
            )
            update()
        }
        camera.update()
        val generator = FreeTypeFontGenerator(Gdx.files.internal("Pizel.ttf"))
        val font = generator.generateFont(FreeTypeFontParameter().apply {
            size = 16
        })
        val debugFont = generator.generateFont(FreeTypeFontParameter().apply {
            size = 8
        })
        generator.dispose()
        with(engine) {
            addSystem(CameraSystem(camera, batch))
            addSystem(TileRenderSystem(batch, camera, debugFont))
            addSystem(AnimationSystem())
            addSystem(PlayerInputSystem())
            addSystem(MovementSystem())
            addSystem(
                RenderSystem(
                    batch,
                    font,
                )
            )
        }
        val baseWorld = WorldManager.getBaseWorld()
        TileMapFactory.createTileMap(
            engine,
            Vector2(0f, 0f),
            baseWorld.tileMapComponent
        )
        PlayerFactory.createPlayer(
            engine,
            Vector2(baseWorld.spawnX, baseWorld.spawnY),
            Vector2(16f, 16f),
            "player",
        )
    }

    override fun resize(width: Int, height: Int) {
        with(camera) {
            setToOrtho(
                false,
                width.toFloat() / Shared.VISUAL_SCALE,
                height.toFloat() / Shared.VISUAL_SCALE
            )
            update()
        }
    }

    override fun render(delta: Float) {
        clearScreen(red = 0.098f, green = 0.1725f, blue = 0.3961f, alpha = 1f)
        with(batch) {
            begin()
            engine.update(delta)
            end()
        }
    }

    override fun dispose() {
        engine.systems.forEach {
            when (it) {
                is RenderSystem -> it.dispose()
                is TileRenderSystem -> it.disposeTextures()
            }
        }
        batch.dispose()
    }
}
