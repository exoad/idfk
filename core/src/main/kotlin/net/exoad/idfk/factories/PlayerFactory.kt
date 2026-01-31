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
        id: String = "player"
    ): Entity {
        assert(id.isNotBlank())
        val player = Entity().apply {
            add(PositionComponent(position.x, position.y))
            add(VelocityComponent(0f, 0f))
            add(
                if (size == null) {
                    SizeComponent(32f, 32f)
                } else {
                    SizeComponent(size.x, size.y)
                }
            )
            add(TextureComponent("logo.png"))
            add(PlayerComponent())
            add(IdComponent(id))
        }
        engine.addEntity(player)
        return player
    }
}