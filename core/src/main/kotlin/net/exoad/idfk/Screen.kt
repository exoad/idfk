package net.exoad.idfk

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import ktx.app.KtxScreen
import ktx.app.clearScreen
import net.exoad.idfk.Shared.Keybinds.isJustPressed
import net.exoad.idfk.ecs.attach
import net.exoad.idfk.ecs.system.*
import net.exoad.idfk.factories.PlayerFactory
import net.exoad.idfk.factories.TileMapFactory
import net.exoad.idfk.util.SpriteSheet
import net.exoad.idfk.world.WorldManager

class Screen : KtxScreen {
    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    private val engine = Engine()
    private val camera = OrthographicCamera()
    private val tileRenderSystem: TileRenderSystem

    init {
        with(camera) {
            setToOrtho(
                false,
                Gdx.graphics.width.toFloat() / Shared.VISUAL_SCALE,
                Gdx.graphics.height.toFloat() / Shared.VISUAL_SCALE
            )
            update()
        }
        val generator = FreeTypeFontGenerator(Gdx.files.internal("Pizel.ttf"))
        val font = generator.generateFont(FreeTypeFontParameter().apply { size = 16 })
        generator.dispose()
        tileRenderSystem = TileRenderSystem(batch, camera, shapeRenderer)
        engine.attach {
            +CameraSystem(camera, batch)
            +tileRenderSystem
            +AnimationSystem()
            +PlayerInputSystem()
            +MovementSystem()
            +CollisionSystem()
            +RenderSystem(batch, font)
        }
        val baseWorld = WorldManager["base"]
        TileMapFactory.createFromWorld(engine, baseWorld, Vec2(0f, 0f))
        PlayerFactory.createPlayer(
            engine,
            Vec2(baseWorld.spawnX, baseWorld.spawnY),
            Vec2(16f, 16f),
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
        clearScreen(0.098f, 0.1725f, 0.3961f)
        if (Shared.Keybinds.SHOW_DEBUG.isJustPressed()) {
            Shared.DEBUG_MODE = !Shared.DEBUG_MODE
        }
        with(batch) {
            begin()
            engine.update(delta)
            end()
        }
        if (Shared.DEBUG_MODE) {
            tileRenderSystem.drawCollisionBoxes()
            tileRenderSystem.drawEntityCollisionBoxes(engine.entities)
        }
    }

    override fun dispose() {
        engine.systems.forEach {
            if (it is DisposableSystem) {
                it.dispose()
            }
        }
        SpriteSheet.dispose()
        batch.dispose()
        shapeRenderer.dispose()
    }
}
