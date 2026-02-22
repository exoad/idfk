package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.Shared
import net.exoad.idfk.Shared.Keybinds.isPressed
import net.exoad.idfk.ecs.component.PlayerComponent
import net.exoad.idfk.ecs.component.VelocityComponent
import net.exoad.idfk.util.Logger
import kotlin.math.sqrt

class PlayerInputSystem : IteratingSystem(allOf(PlayerComponent::class, VelocityComponent::class).get()) {
    private val velocityMapper = mapperFor<VelocityComponent>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val velocity = entity[velocityMapper]!!
        val goWest = Shared.Keybinds.MOVE_WEST.isPressed()
        val goEast = Shared.Keybinds.MOVE_EAST.isPressed()
        val goNorth = Shared.Keybinds.MOVE_NORTH.isPressed()
        val goSouth = Shared.Keybinds.MOVE_SOUTH.isPressed()
        val goSprint = Shared.Keybinds.SPRINT.isPressed()
        var dx = when {
            goWest && !goEast -> -1f
            goEast && !goWest -> 1f
            else -> 0f
        }
        var dy = when {
            goNorth && !goSouth -> 1f
            goSouth && !goNorth -> -1f
            else -> 0f
        }
        if (dx != 0f && dy != 0f) {
            val len = sqrt(dx * dx + dy * dy)
            if (len != 0f) {
                dx /= len
                dy /= len
            }
        }
        val speed = Shared.Player.MAX_SPEED * if (goSprint) Shared.Player.SPRINT_MULTIPLIER else 1f
        velocity.x = dx * speed
        velocity.y = dy * speed
    }
}
