package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.ecs.component.*

abstract class PlayerAwareSystem(family: Family) : IteratingSystem(family) {
    protected var playerEntity: Entity? = null
    private val idMapper = mapperFor<IdComponent>()

    protected fun ensurePlayer() {
        if (playerEntity == null) {
            engine.entities.forEach {
                if (it[idMapper]?.id == "player") {
                    playerEntity = it
                    return@forEach
                }
            }
        }
    }

    protected inline fun <reified T : Component> playerComponent(mapperProvider: () -> com.badlogic.ashley.core.ComponentMapper<T>): T? {
        ensurePlayer()
        val e = playerEntity ?: return null
        return e[mapperProvider()]
    }

    protected fun playerPosition(): PositionComponent? {
        return playerComponent { mapperFor<PositionComponent>() }
    }

    protected fun playerSize(): SizeComponent? {
        return playerComponent { mapperFor<SizeComponent>() }
    }

    protected fun playerHealth(): HealthComponent? {
        return playerComponent { mapperFor<HealthComponent>() }
    }

    protected fun playerComp(): PlayerComponent? {
        return playerComponent { mapperFor<PlayerComponent>() }
    }

    protected fun playerVelocity(): VelocityComponent? {
        return playerComponent { mapperFor<VelocityComponent>() }
    }

    override fun update(deltaTime: Float) {
        ensurePlayer()
        super.update(deltaTime)
    }
}
