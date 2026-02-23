package net.exoad.idfk.util

import net.exoad.idfk.Shared

object CoordinateConverter {
    inline val TILE_SIZE: Int
        get() = Shared.World.TILE_SIZE

    fun tilesToWorldPixels(tileX: Int, tileY: Int): Pair<Float, Float> {
        return Pair(
            tileX * TILE_SIZE.toFloat(),
            tileY * TILE_SIZE.toFloat()
        )
    }

    fun tilesToWorldPixels(tileX: Float, tileY: Float): Pair<Float, Float> {
        return Pair(
            tileX * TILE_SIZE,
            tileY * TILE_SIZE
        )
    }

    fun worldPixelsToTiles(worldX: Float, worldY: Float): Pair<Int, Int> {
        return Pair(
            (worldX / TILE_SIZE).toInt(),
            (worldY / TILE_SIZE).toInt()
        )
    }

    fun worldPixelsToTileCoordinate(worldX: Float, worldY: Float): TileCoordinate {
        return TileCoordinate(
            (worldX / TILE_SIZE).toInt(),
            (worldY / TILE_SIZE).toInt()
        )
    }

    fun tileCoordinateToWorldPixels(tileCoord: TileCoordinate): Pair<Float, Float> {
        return tilesToWorldPixels(tileCoord.x, tileCoord.y)
    }

    fun getTileCenterWorldPixels(tileX: Int, tileY: Int): Pair<Float, Float> {
        return Pair(
            tileX * TILE_SIZE + TILE_SIZE / 2f,
            tileY * TILE_SIZE + TILE_SIZE / 2f
        )
    }

    fun getTileCenterWorldPixels(tileCoord: TileCoordinate): Pair<Float, Float> {
        return getTileCenterWorldPixels(tileCoord.x, tileCoord.y)
    }
}

