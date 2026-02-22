package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.ecs.component.MovementIntentComponent
import net.exoad.idfk.ecs.component.PlayerComponent
import net.exoad.idfk.ecs.component.VelocityComponent

class MovementSystem : IteratingSystem(
    allOf(
        VelocityComponent::class,
        MovementIntentComponent::class,
        PlayerComponent::class
    ).get()
) {
    private val velocityMapper = mapperFor<VelocityComponent>()
    private val movementIntentMapper = mapperFor<MovementIntentComponent>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val velocity = entity[velocityMapper]!!
        with(entity[movementIntentMapper]!!) {
            dx = velocity.x * deltaTime
            dy = velocity.y * deltaTime
        }
    }
}
