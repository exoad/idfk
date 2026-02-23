package net.exoad.idfk.util

import kotlin.math.sqrt

data class TileCoordinate(val x: Int, val y: Int) {
    fun toWorldPixels(): Pair<Float, Float> {
        return CoordinateConverter.tilesToWorldPixels(x, y)
    }

    fun distanceTo(other: TileCoordinate): Double {
        val dx = (other.x - this.x).toDouble()
        val dy = (other.y - this.y).toDouble()
        return sqrt(dx * dx + dy * dy)
    }

    fun isWithinRange(other: TileCoordinate, range: Int): Boolean {
        return distanceTo(other) <= range
    }

    companion object {
        fun fromWorldPixels(worldX: Float, worldY: Float): TileCoordinate {
            return CoordinateConverter.worldPixelsToTileCoordinate(worldX, worldY)
        }

        fun fromWorldPixels(worldX: Int, worldY: Int): TileCoordinate {
            return CoordinateConverter.worldPixelsToTileCoordinate(worldX.toFloat(), worldY.toFloat())
        }
    }
}

