package net.exoad.idfk.util

import net.exoad.idfk.Bool
import kotlin.math.ceil
import kotlin.math.floor

class TileGrid(val width: Int, val height: Int) {
    private val cells = Array(width * height) { TileCell() }

    private fun idx(x: Int, y: Int): Int {
        return y * width + x
    }

    fun getCell(x: Int, y: Int): TileCell? {
        return if (x in 0 until width && y in 0 until height) cells[idx(x, y)] else null
    }

    fun placeObject(x: Int, y: Int, obj: WorldObject): Bool {
        (getCell(x, y) ?: return false).objects.add(obj)
        return true
    }

    fun removeObject(x: Int, y: Int): Bool {
        return (getCell(x, y) ?: return false).objects.removeIf { true }
    }

    fun removeObject(x: Int, y: Int, obj: WorldObject): Bool {
        return (getCell(x, y) ?: return false).objects.remove(obj)
    }

    fun objectsAt(x: Int, y: Int): List<WorldObject> {
        return getCell(x, y)?.objects ?: emptyList()
    }

    fun moveObject(fromX: Int, fromY: Int, toX: Int, toY: Int, obj: WorldObject): Bool {
        return if (!removeObject(fromX, fromY, obj)) false else placeObject(toX, toY, obj)
    }

    fun allObjects(): List<WorldObject> {
        return cells.flatMap { it.objects }
    }

    fun isAreaBlocked(rectX: Float, rectY: Float, rectW: Float, rectH: Float, tileSize: Int = 16): Boolean {
        val startX = floor((rectX / tileSize).toDouble()).toInt().coerceIn(0, width - 1)
        val startY = floor((rectY / tileSize).toDouble()).toInt().coerceIn(0, height - 1)
        val endX = ceil(((rectX + rectW) / tileSize).toDouble()).toInt().minus(1).coerceIn(0, width - 1)
        val endY = ceil(((rectY + rectH) / tileSize).toDouble()).toInt().minus(1).coerceIn(0, height - 1)
        for (ty in startY..endY) {
            for (tx in startX..endX) {
                val cell = getCell(tx, ty) ?: continue
                for (obj in cell.objects) {
                    if (!obj.blocking) {
                        continue
                    }
                    if (rectsOverlap(
                            rectX,
                            rectY,
                            rectW,
                            rectH,
                            tx * tileSize + obj.offsetX,
                            ty * tileSize + obj.offsetY,
                            obj.width,
                            obj.height
                        )
                    ) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun rectsOverlap(
        ax: Float,
        ay: Float,
        aw: Float,
        ah: Float,
        bx: Float,
        by: Float,
        bw: Float,
        bh: Float
    ): Boolean {
        // Use <= and >= to include edge touching as collision
        return ax < bx + bw && ax + aw > bx && ay < by + bh && ay + ah > by
    }

    fun isTileBlocked(x: Int, y: Int): Boolean {
        return (getCell(x, y) ?: return true).objects.any { it.blocking }
    }
}
