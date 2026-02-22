package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.ecs.component.PlayerComponent
import net.exoad.idfk.ecs.component.PositionComponent
import net.exoad.idfk.ecs.component.VelocityComponent
import net.exoad.idfk.util.Logger
import kotlin.math.round

class MovementSystem : IteratingSystem(
    allOf(
        PositionComponent::class,
        VelocityComponent::class,
        PlayerComponent::class
    ).get()
) {
    private val positionMapper = mapperFor<PositionComponent>()
    private val velocityMapper = mapperFor<VelocityComponent>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity[positionMapper]!!
        val velocity = entity[velocityMapper]!!
        position.x += velocity.x * deltaTime
        position.y += velocity.y * deltaTime
        // apply snapping to grid for the pixels
        position.x = round(position.x)
        position.y = round(position.y)
        Logger.info("Entity $entity moved to (${position.x}, ${position.y})")
    }
}
