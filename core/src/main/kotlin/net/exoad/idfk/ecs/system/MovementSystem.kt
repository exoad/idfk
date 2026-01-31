package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.ecs.component.PlayerComponent
import net.exoad.idfk.ecs.component.VelocityComponent
import net.exoad.idfk.shared.WorldConfig

class MovementSystem : IteratingSystem(
    allOf(
        VelocityComponent::class,
        PlayerComponent::class
    ).get()
) {
    private val velocityMapper = mapperFor<VelocityComponent>()
    private val playerMapper = mapperFor<PlayerComponent>()

    companion object {
        const val COYOTE_TIME = 0.12f
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val velocity = entity[velocityMapper]!!
        val player = entity[playerMapper]!!
        velocity.y -= WorldConfig.GRAVITY * deltaTime
        if (velocity.y < WorldConfig.TERMINAL_VELOCITY) {
            velocity.y = WorldConfig.TERMINAL_VELOCITY
        }
        if (velocity.y < 0 && player.grounded) {
            // leaving the ground, start coyote timer
            player.grounded = false
            player.coyoteTimer = COYOTE_TIME
        }
        if (!player.grounded && player.coyoteTimer > 0f) {
            player.coyoteTimer -= deltaTime
            if (player.coyoteTimer < 0f) player.coyoteTimer = 0f
        }
    }
}
