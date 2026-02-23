package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.Shared
import net.exoad.idfk.Shared.Keybinds.isJustPressed
import net.exoad.idfk.ecs.component.PlayerComponent
import net.exoad.idfk.ecs.component.PositionComponent
import net.exoad.idfk.util.TileCoordinate
import kotlin.math.abs

class CrosshairMovementSystem : IteratingSystem(
    allOf(
        PlayerComponent::class,
        PositionComponent::class,
    ).get()
) {
    private val positionMapper = mapperFor<PositionComponent>()
    private var crosshairTile = TileCoordinate(0, 0)
    private var isInitialized = false

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val playerPosition = entity[positionMapper] ?: return
        if (!isInitialized) {
            crosshairTile = TileCoordinate.fromWorldPixels(playerPosition.x, playerPosition.y)
            isInitialized = true
        }
        if (Shared.Keybinds.LOOK_NORTH.isJustPressed()) {
            crosshairTile = crosshairTile.copy(y = crosshairTile.y + 1)
        }
        if (Shared.Keybinds.LOOK_SOUTH.isJustPressed()) {
            crosshairTile = crosshairTile.copy(y = crosshairTile.y - 1)
        }
        if (Shared.Keybinds.LOOK_WEST.isJustPressed()) {
            crosshairTile = crosshairTile.copy(x = crosshairTile.x - 1)
        }
        if (Shared.Keybinds.LOOK_EAST.isJustPressed()) {
            crosshairTile = crosshairTile.copy(x = crosshairTile.x + 1)
        }
        val playerTile = TileCoordinate.fromWorldPixels(playerPosition.x, playerPosition.y)
        if (!crosshairTile.isWithinRange(playerTile, Shared.Player.MAX_REACH_RANGE)) {
            val dx = crosshairTile.x - playerTile.x
            val dy = crosshairTile.y - playerTile.y
            crosshairTile = when {
                abs(dx) > abs(dy) -> crosshairTile.copy(x = crosshairTile.x + if (dx > 0) -1 else 1)
                else -> crosshairTile.copy(y = crosshairTile.y + if (dy > 0) -1 else 1)
            }
        }
    }

    fun getCrosshairTile(): TileCoordinate {
        return crosshairTile
    }

    fun getCrosshairWorldX(): Float {
        return crosshairTile.toWorldPixels().first
    }

    fun getCrosshairWorldY(): Float {
        return crosshairTile.toWorldPixels().second
    }
}

