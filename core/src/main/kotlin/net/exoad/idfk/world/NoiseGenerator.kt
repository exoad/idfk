package net.exoad.idfk.world

import net.exoad.idfk.Str
import kotlin.math.floor
import kotlin.random.Random

object PerlinNoiseGenerator {
    private const val PERMUTATION_SIZE = 256
    private val permutation = IntArray(PERMUTATION_SIZE * 2)

    init {
        val rng = Random(12345L)
        for (i in 0 until PERMUTATION_SIZE) {
            permutation[i] = i
        }
        for (i in PERMUTATION_SIZE - 1 downTo 1) {
            val j = rng.nextInt(i + 1)
            val temp = permutation[i]
            permutation[i] = permutation[j]
            permutation[j] = temp
        }
        for (i in 0 until PERMUTATION_SIZE) {
            permutation[PERMUTATION_SIZE + i] = permutation[i]
        }
    }

    private fun fade(t: Float): Float {
        return t * t * t * (t * (t * 6f - 15f) + 10f)
    }

    private fun lerp(t: Float, a: Float, b: Float): Float {
        return a + t * (b - a)
    }

    private fun gradient(hash: Int, x: Float, y: Float): Float {
        val h = hash and 15
        val u = if (h < 8) x else y
        val v = if (h < 8) y else x
        return (if ((h and 1) == 0) u else -u) + (if ((h and 2) == 0) v else -v)
    }

    fun noise(x: Float, y: Float): Float {
        val xi = floor(x).toInt() and 255
        val yi = floor(y).toInt() and 255
        val xf = x - floor(x)
        val yf = y - floor(y)
        val u = fade(xf)
        val p0 = permutation[xi] + yi
        val p1 = permutation[xi + 1] + yi
        return lerp(
            fade(yf),
            lerp(
                u,
                gradient(
                    permutation[p0],
                    xf,
                    yf
                ),
                gradient(
                    permutation[p1],
                    xf - 1f,
                    yf
                )
            ),
            lerp(
                u,
                gradient(
                    permutation[p0 + 1],
                    xf,
                    yf - 1f
                ),
                gradient(
                    permutation[p1 + 1],
                    xf - 1f,
                    yf - 1f
                )
            )
        )
    }

    fun fractionalBrownianMotion(
        x: Float,
        y: Float,
        octaves: Int = 4,
        persistence: Float = 0.5f,
        lacunarity: Float = 2f
    ): Float {
        var value = 0f
        var amplitude = 1f
        var frequency = 1f
        var maxValue = 0f
        repeat(octaves) {
            value += amplitude * noise(x * frequency, y * frequency)
            maxValue += amplitude
            amplitude *= persistence
            frequency *= lacunarity
        }
        return value / maxValue
    }
}

object NoiseBasedWorldGenerator {

    fun generateObjectPositions(
        width: Int,
        height: Int,
        density: Float = 0.3f,
        scale: Float = 0.1f,
        seed: Long = System.currentTimeMillis()
    ): List<Pair<Int, Int>> {
        val positions = mutableListOf<Pair<Int, Int>>()
        val rng = Random(seed)
        for (y in 0 until height) {
            for (x in 0 until width) {
                if ((PerlinNoiseGenerator.fractionalBrownianMotion(
                        x = x * scale,
                        y = y * scale,
                        octaves = 3,
                        persistence = 0.6f,
                        lacunarity = 2f
                    ) + 1f) / 2f > 0.5f && rng.nextFloat() < density
                ) {
                    positions.add(x to y)
                }
            }
        }
        return positions
    }

    fun generateTreePositions(
        width: Int,
        height: Int,
        treeDensity: Float = 0.2f,
        seed: Long = System.currentTimeMillis()
    ): List<Pair<Int, Int>> {
        return generateObjectPositions(
            width = width,
            height = height,
            density = treeDensity.coerceIn(0f, 1f),
            scale = 0.08f,
            seed = seed
        )
    }

    fun generateObjectPositionsWithSeed(
        width: Int,
        height: Int,
        density: Float = 0.3f,
        scale: Float = 0.1f,
        seed: Long = System.currentTimeMillis(),
        objectType: Str = "object"
    ): List<Pair<Int, Int>> {
        val typedSeed = seed + objectType.hashCode()
        return generateObjectPositions(
            width = width,
            height = height,
            density = density,
            scale = scale,
            seed = typedSeed
        )
    }
}

