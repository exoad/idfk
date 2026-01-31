package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.shared.MovementConfig
import net.exoad.idfk.ecs.component.PlayerComponent
import net.exoad.idfk.ecs.component.VelocityComponent

class MovementSystem : IteratingSystem(allOf(VelocityComponent::class, PlayerComponent::class).get()) {
    private val velocityMapper = mapperFor<VelocityComponent>()
    private val playerMapper = mapperFor<PlayerComponent>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val velocity = entity[velocityMapper]!!
        val player = entity[playerMapper]!!
        velocity.y -= MovementConfig.GRAVITY * deltaTime
        if (velocity.y < MovementConfig.TERMINAL_VELOCITY) {
            velocity.y = MovementConfig.TERMINAL_VELOCITY
        }
        if (velocity.y < 0) {
            player.grounded = false
        }
    }
}
