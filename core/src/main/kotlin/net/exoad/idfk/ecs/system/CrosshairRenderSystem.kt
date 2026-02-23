package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Cursor
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import net.exoad.idfk.Shared
import net.exoad.idfk.util.CoordinateConverter
import net.exoad.idfk.util.SpriteRegistry

class CrosshairRenderSystem(
    private val batch: SpriteBatch,
    private val crosshairMovementSystem: CrosshairMovementSystem
) : EntitySystem(), DisposableSystem {

    init {
        Gdx.input.isCursorCatched = false
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None)
    }

    override fun update(deltaTime: Float) {
        val crosshairRegion = SpriteRegistry.getRegion("crosshair")
        if (crosshairRegion != null) {
            val crosshairTile = crosshairMovementSystem.getCrosshairTile()
            val (centerX, centerY) = CoordinateConverter.getTileCenterWorldPixels(crosshairTile)
            val tileSize = Shared.World.TILE_SIZE.toFloat()
            with(batch) {
                color = Color.WHITE
                draw(
                    crosshairRegion,
                    centerX - tileSize / 2f,
                    centerY - tileSize / 2f,
                    tileSize,
                    tileSize
                )
            }
        }
    }

    override fun dispose() {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow)
    }
}


