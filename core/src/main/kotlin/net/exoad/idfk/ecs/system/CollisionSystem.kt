package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.ecs.component.CollisionComponent
import net.exoad.idfk.ecs.component.MovementIntentComponent
import net.exoad.idfk.ecs.component.PositionComponent
import net.exoad.idfk.ecs.component.VelocityComponent
import net.exoad.idfk.world.WorldManager

class CollisionSystem : IteratingSystem(
    allOf(
        PositionComponent::class,
        MovementIntentComponent::class,
        CollisionComponent::class
    ).get()
) {
    private val positionMapper = mapperFor<PositionComponent>()
    private val intentMapper = mapperFor<MovementIntentComponent>()
    private val collisionMapper = mapperFor<CollisionComponent>()
    private val velocityMapper = mapperFor<VelocityComponent>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity[positionMapper]!!
        with(entity[intentMapper]!!) {
            val collision = entity[collisionMapper]!!
            val velocity = entity[velocityMapper]
            val grid = WorldManager["base"].objectGrid
            val collisionX = position.x + collision.offsetX
            val collisionY = position.y + collision.offsetY
            val newX = position.x + dx
            val newCollisionX = newX + collision.offsetX
            if (!grid.isAreaBlocked(newCollisionX, collisionY, collision.width, collision.height, 16)) {
                position.x = newX
            } else {
                dx = 0f
                velocity?.x = 0f
            }
            val newY = position.y + dy
            val newCollisionY = newY + collision.offsetY
            if (!grid.isAreaBlocked(collisionX, newCollisionY, collision.width, collision.height, 16)) {
                position.y = newY
            } else {
                dy = 0f
                velocity?.y = 0f
            }
            dx = 0f
            dy = 0f
        }
    }
}

