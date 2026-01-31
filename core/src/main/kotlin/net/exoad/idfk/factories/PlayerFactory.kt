package net.exoad.idfk.factories

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import net.exoad.idfk.ecs.component.*
import net.exoad.idfk.ecs.attach

object PlayerFactory {
    fun createPlayer(
        engine: Engine,
        position: Vector2,
        size: Vector2? = null,
        id: String = "player",
        health: Int,
        maxHealth: Int
    ): Entity {
        assert(id.isNotBlank())
        val player = Entity().attach {
            +PositionComponent(position.x, position.y)
            +VelocityComponent(0f, 0f)
            if (size == null) {
                +SizeComponent(96f, 72f)
            } else {
                +SizeComponent(size.x, size.y)
            }
            +TextureComponent("logo.png")
            +PlayerComponent()
            +IdComponent(id)
            +HealthComponent(health, maxHealth)
        }
        engine.addEntity(player)
        return player
    }
}