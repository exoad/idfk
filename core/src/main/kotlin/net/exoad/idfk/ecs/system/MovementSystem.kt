package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.ecs.component.PlayerComponent
import net.exoad.idfk.ecs.component.PositionComponent
import net.exoad.idfk.ecs.component.VelocityComponent

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
        val velocity = entity[velocityMapper]!!
        with(entity[positionMapper]!!) {
            x += velocity.x * deltaTime
            y += velocity.y * deltaTime
        }
    }
}
