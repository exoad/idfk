package net.exoad.idfk.ecs

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem

@JvmInline
value class EntityDSL(val entity: Entity) {
    operator fun Component.unaryPlus() {
        entity.add(this)
    }
}

fun Entity.attach(block: EntityDSL.() -> Unit): Entity {
    EntityDSL(this).block()
    return this
}

@JvmInline
value class EngineDSL(val engine: Engine) {
    operator fun EntitySystem.unaryPlus() {
        engine.addSystem(this)
    }
}

fun Engine.attach(block: EngineDSL.() -> Unit): Engine {
    EngineDSL(this).block()
    return this
}
