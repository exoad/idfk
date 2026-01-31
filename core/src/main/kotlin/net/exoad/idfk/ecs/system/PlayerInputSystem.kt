package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.ecs.component.PlayerComponent
import net.exoad.idfk.ecs.component.VelocityComponent
import net.exoad.idfk.shared.KeyBindings
import net.exoad.idfk.shared.MovementConfig

class PlayerInputSystem : IteratingSystem(
    allOf(
        PlayerComponent::class,
        VelocityComponent::class
    ).get()
) {
    private val velocityMapper = mapperFor<VelocityComponent>()
    private val playerMapper = mapperFor<PlayerComponent>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val velocity = entity[velocityMapper]!!
        val player = entity[playerMapper]!!
        val left = Gdx.input.isKeyPressed(KeyBindings.MOVE_LEFT)
        val right = Gdx.input.isKeyPressed(KeyBindings.MOVE_RIGHT)
        val sprintPressed =
            Gdx.input.isKeyPressed(KeyBindings.SPRINT) || Gdx.input.isKeyPressed(
                Input.Keys.SHIFT_RIGHT
            )
        velocity.x = when {
            left && !right -> -1f
            right && !left -> 1f
            else -> 0f
        } * MovementConfig.MAX_SPEED * if (sprintPressed) {
            MovementConfig.SPRINT_SPEED_MULTIPLIER
        } else {
            1f
        }
        val canJump = player.grounded || player.coyoteTimer > 0f
        if (Gdx.input.isKeyJustPressed(KeyBindings.JUMP) && canJump) {
            velocity.y = MovementConfig.JUMP_VELOCITY
            player.grounded = false
            player.coyoteTimer = 0f
        }

    }
}
