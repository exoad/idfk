package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.ecs.component.DirectionComponent
import net.exoad.idfk.ecs.component.PlayerComponent
import net.exoad.idfk.ecs.component.PositionComponent
import net.exoad.idfk.util.TileCoordinate
import net.exoad.idfk.world.Direction
import kotlin.math.abs

class CrosshairDirectionSystem(
    private val crosshairMovementSystem: CrosshairMovementSystem
) : IteratingSystem(
    allOf(
        PlayerComponent::class,
        PositionComponent::class,
        DirectionComponent::class,
    ).get()
) {
    private val positionMapper = mapperFor<PositionComponent>()
    private val directionMapper = mapperFor<DirectionComponent>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity[positionMapper] ?: return
        val playerTile = TileCoordinate.fromWorldPixels(position.x, position.y)
        val crosshairTile = crosshairMovementSystem.getCrosshairTile()
        val dx = crosshairTile.x - playerTile.x
        val dy = crosshairTile.y - playerTile.y
        (entity[directionMapper] ?: return).direction = when {
            abs(dx) >= abs(dy) -> if (dx > 0) Direction.EAST else Direction.WEST
            else -> if (dy > 0) Direction.NORTH else Direction.SOUTH
        }
    }
}

