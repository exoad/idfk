package net.exoad.idfk

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

object Shared {
    const val VISUAL_SCALE = 5f
    var DEBUG_MODE = false

    object Player {
        const val MAX_SPEED = 120f
        const val SPRINT_MULTIPLIER = 1.18f
        const val MAX_REACH_RANGE = 3
    }

    object World {
        const val SPAWN_X = 0f
        const val SPAWN_Y = 0f
        const val TILE_SIZE = 16
    }

    object Keybinds {
        const val MOVE_NORTH = Input.Keys.W
        const val MOVE_SOUTH = Input.Keys.S
        const val MOVE_EAST = Input.Keys.D
        const val MOVE_WEST = Input.Keys.A
        const val SPRINT = Input.Keys.SHIFT_LEFT
        const val SHOW_DEBUG = Input.Keys.G
        const val LOOK_NORTH = Input.Keys.I
        const val LOOK_WEST = Input.Keys.J
        const val LOOK_SOUTH = Input.Keys.K
        const val LOOK_EAST = Input.Keys.L
        const val INTERACT = Input.Keys.SPACE

        fun Int.isPressed(): Bool {
            return when (this) {
                Input.Buttons.LEFT, Input.Buttons.RIGHT -> Gdx.input.isButtonPressed(this)
                else -> Gdx.input.isKeyPressed(this)
            }
        }

        fun Int.isJustPressed(): Bool {
            return Gdx.input.isKeyJustPressed(this)
        }
    }
}
