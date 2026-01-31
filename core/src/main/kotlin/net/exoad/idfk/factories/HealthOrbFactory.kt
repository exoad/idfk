package net.exoad.idfk.factories

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import net.exoad.idfk.ecs.component.*

object HealthOrbFactory {
    fun createHealthOrb(
        engine: Engine,
        position: Vector2,
        amount: Int,
        size: Vector2? = null,
        id: String? = null,
        bobAmplitude: Float = 4f,
        bobSpeed: Float = 2f
    ): Entity {
        val orb = Entity().apply {
            add(PositionComponent(position.x, position.y))
            add(
                if (size == null) {
                    SizeComponent(32f, 32f)
                } else {
                    SizeComponent(size.x, size.y)
                }
            )
            add(TextureComponent("logo.png"))
            add(
                HealthOrbComponent(
                    amount,
                    bobAmplitude,
                    bobSpeed,
                    position.y,
                    0f
                )
            )
            if (id != null) {
                add(IdComponent(id))
            }
            add(
                ColorComponent(
                    if (amount >= 0) {
                        Color.GREEN
                    } else {
                        Color.RED
                    }
                )
            )
        }
        engine.addEntity(orb)
        return orb
    }
}
