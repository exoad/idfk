package net.exoad.idfk

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

object Shared {
    const val VISUAL_SCALE = 3.5f
    const val DEBUG = true

    object Player {
        const val MAX_SPEED = 200f
        const val SPRINT_MULTIPLIER = 1.8f
    }


    object Keybinds {
        const val MOVE_NORTH = Input.Keys.W
        const val MOVE_SOUTH = Input.Keys.S
        const val MOVE_EAST = Input.Keys.D
        const val MOVE_WEST = Input.Keys.A
        const val SPRINT = Input.Keys.SHIFT_LEFT

        fun Int.isPressed(): Boolean {
            return Gdx.input.isKeyPressed(this)
        }
    }
}
