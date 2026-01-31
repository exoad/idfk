package net.exoad.idfk.ecs.component

import com.badlogic.ashley.core.Component

@JvmInline
value class PlatformComponent(val collisionMask: Int) : Component {
    companion object {
        const val COLLIDE_NORTH = 1
        const val COLLIDE_SOUTH = 2
        const val COLLIDE_WEST = 4
        const val COLLIDE_EAST = 8
        const val COLLIDE_ALL =
            COLLIDE_NORTH or COLLIDE_SOUTH or COLLIDE_WEST or COLLIDE_EAST
    }

    fun canCollide(side: Int): Boolean {
        return (collisionMask and side) != 0
    }
}
