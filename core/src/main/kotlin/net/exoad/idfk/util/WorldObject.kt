package net.exoad.idfk.util

import net.exoad.idfk.Bool
import net.exoad.idfk.Str

data class WorldObject(
    val id: Int,
    val type: Str,
    val width: Float = CoordinateConverter.TILE_SIZE.toFloat(),
    val height: Float = CoordinateConverter.TILE_SIZE.toFloat(),
    val offsetX: Float = 0f,
    val offsetY: Float = 0f,
    val blocking: Bool = true,
)
