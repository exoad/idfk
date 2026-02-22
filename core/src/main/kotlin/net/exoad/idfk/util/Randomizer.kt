package net.exoad.idfk.util

import com.badlogic.gdx.graphics.Color
import net.exoad.idfk.Bool

object Randomizer {
    fun randomColor(applyAlpha: Bool = false): Color {
        return Color(
            (0..255).random() / 255f,
            (0..255).random() / 255f,
            (0..255).random() / 255f,
            if (!applyAlpha) 1f else (0..255).random() / 255f
        )
    }
}
