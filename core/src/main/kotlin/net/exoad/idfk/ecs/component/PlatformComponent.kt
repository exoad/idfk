package net.exoad.idfk.ecs.component

import com.badlogic.ashley.core.Component

@JvmInline
value class PlatformComponent(val collisionMask: Int) : Component {
    companion object {
        const val COLLIDE_TOP = 1
        const val COLLIDE_BOTTOM = 2
        const val COLLIDE_LEFT = 4
        const val COLLIDE_RIGHT = 8
        const val COLLIDE_ALL = COLLIDE_TOP or COLLIDE_BOTTOM or COLLIDE_LEFT or COLLIDE_RIGHT
    }

    fun canCollide(side: Int): Boolean {
        return (collisionMask and side) != 0
    }
}
