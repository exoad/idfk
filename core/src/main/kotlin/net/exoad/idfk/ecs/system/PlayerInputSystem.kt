package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.shared.MovementConfig
import net.exoad.idfk.ecs.component.PlayerComponent
import net.exoad.idfk.ecs.component.VelocityComponent

class PlayerInputSystem : IteratingSystem(allOf(PlayerComponent::class, VelocityComponent::class).get()) {
    private val velocityMapper = mapperFor<VelocityComponent>()
    private val playerMapper = mapperFor<PlayerComponent>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val velocity = entity[velocityMapper]!!
        val player = entity[playerMapper]!!
        val left = Gdx.input.isKeyPressed(Input.Keys.A)
        val right = Gdx.input.isKeyPressed(Input.Keys.D)
        val inputDir = when {
            left && !right -> -1f
            right && !left -> 1f
            else -> 0f
        }
        velocity.x = inputDir * MovementConfig.MAX_SPEED
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && player.grounded) {
            velocity.y = MovementConfig.JUMP_VELOCITY
            player.grounded = false
        }

    }
}
