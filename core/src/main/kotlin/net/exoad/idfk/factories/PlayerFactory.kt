package net.exoad.idfk.factories

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import net.exoad.idfk.ecs.component.*

object PlayerFactory {
    fun createPlayer(
        engine: Engine,
        position: Vector2,
        size: Vector2? = null,
        id: String = "player",
    ): Entity {
        assert(id.isNotBlank())
        val playerSize = size ?: Vector2(16f, 16f)
        val player = Entity().apply {
            add(IdComponent(id))
            add(PositionComponent(position.x, position.y))
            add(VelocityComponent(0f, 0f))
            add(SizeComponent(playerSize.x, playerSize.y))
            add(DirectionComponent())
            add(
                AtlasComponent(
                    texturePath = "player.png",
                    frameWidth = 16,
                    frameHeight = 16,
                    framesPerRow = 3
                )
            )
            add(
                AnimationComponent(
                    frames = intArrayOf(0),
                    frameDuration = 0.15f,
                    looping = true,
                    isPlaying = false
                )
            )
            add(PlayerComponent())
        }
        engine.addEntity(player)
        return player
    }
}
