package net.exoad.idfk

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

object Shared {
    var VISUAL_SCALE = 4.8f

    object Player {
        const val MAX_SPEED = 120f
        const val SPRINT_MULTIPLIER = 1.18f
    }

    object World {
        const val SPAWN_X = 0f
        const val SPAWN_Y = 0f
    }

    object Keybinds {
        const val MOVE_NORTH = Input.Keys.W
        const val MOVE_SOUTH = Input.Keys.S
        const val MOVE_EAST = Input.Keys.D
        const val MOVE_WEST = Input.Keys.A
        const val SPRINT = Input.Keys.SHIFT_LEFT

        fun Int.isPressed(): Bool {
            return Gdx.input.isKeyPressed(this)
        }
    }
}
