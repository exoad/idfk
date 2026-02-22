package net.exoad.idfk.world

import net.exoad.idfk.Str
import kotlin.random.Random

object WorldGenerator {
    private val rng = Random(System.currentTimeMillis())

    fun generateWorldAsString(width: Int, height: Int, textureIndices: IntArray): Str {
        return buildString {
            for (y in 0 until height) {
                for (x in 0 until width) {
                    append(textureIndices[rng.nextInt(textureIndices.size)])
                    if (x < width - 1) {
                        append(" ")
                    }
                }
                if (y < height - 1) {
                    appendLine()
                }
            }
        }
    }
}
