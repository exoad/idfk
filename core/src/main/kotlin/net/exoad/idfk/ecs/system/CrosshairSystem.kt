package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Cursor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import net.exoad.idfk.util.SpriteRegistry

class CrosshairSystem(
    private val batch: SpriteBatch,
    private val camera: OrthographicCamera
) : EntitySystem(), DisposableSystem {

    init {
        Gdx.input.isCursorCatched = false
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None)
    }

    override fun update(deltaTime: Float) {
        val mouseX = Gdx.input.x.toFloat()
        val mouseY = Gdx.input.y.toFloat()
        val worldPos = camera.unproject(com.badlogic.gdx.math.Vector3(mouseX, mouseY, 0f))
        val crosshairRegion = SpriteRegistry.getRegion("crosshair")
        if (crosshairRegion != null) {
            with(batch) {
                color = Color.WHITE
                draw(
                    crosshairRegion,
                    worldPos.x - 32f,
                    worldPos.y - 32f,
                    64f,
                    64f
                )
            }
        }
    }

    override fun dispose() {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow)
    }
}


