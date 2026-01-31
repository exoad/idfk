package net.exoad.idfk.factories

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import net.exoad.idfk.ecs.component.*

object PlatformFactory {
    fun createPlatform(
        engine: Engine,
        position: Vector2,
        collisionTypes: Array<Int>,
        size: Vector2? = null,
        id: String? = null
    ): Entity {
        val platform = Entity().apply {
            add(PositionComponent(position.x, position.y))
            add(
                if (size == null) {
                    SizeComponent(300f, 45f)
                } else {
                    SizeComponent(size.x, size.y)
                }
            )
            add(TextureComponent("logo.png"))
            add(PlatformComponent(collisionTypes.fold(0) { acc, type ->
                acc or type
            }))
            if (id != null) {
                add(IdComponent(id))
            }
        }
        engine.addEntity(platform)
        return platform
    }
}