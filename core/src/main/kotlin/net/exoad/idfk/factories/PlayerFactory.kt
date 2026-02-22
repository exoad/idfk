package net.exoad.idfk.factories

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import net.exoad.idfk.Str
import net.exoad.idfk.Vec2
import net.exoad.idfk.ecs.component.*

object PlayerFactory {
    fun createPlayer(
        engine: Engine,
        position: Vec2,
        size: Vec2? = null,
        id: Str = "player",
    ): Entity {
        assert(id.isNotBlank())
        val playerSize = size ?: Vec2(16f, 16f)
        val player = Entity().apply {
            add(IdComponent(id))
            add(PositionComponent(position.x, position.y))
            add(VelocityComponent(0f, 0f))
            add(SizeComponent(playerSize.x, playerSize.y))
            add(DirectionComponent())
            add(AtlasComponent("player.png", 16, 16, 3))
            add(AnimationComponent(intArrayOf(0), 0.15f, looping = true, isPlaying = false))
            add(PlayerComponent())
        }
        engine.addEntity(player)
        return player
    }
}
