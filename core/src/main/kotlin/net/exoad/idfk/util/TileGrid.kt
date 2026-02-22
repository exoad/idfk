package net.exoad.idfk.util

import net.exoad.idfk.Bool

class TileGrid(val width: Int, val height: Int) {
    private val cells = Array(width * height) { TileCell() }

    private fun idx(x: Int, y: Int): Int {
        return y * width + x
    }

    fun getCell(x: Int, y: Int): TileCell? {
        return if (x in 0 until width && y in 0 until height) cells[idx(x, y)] else null
    }

    fun placeObject(x: Int, y: Int, obj: GameObject): Bool {
        (getCell(x, y) ?: return false).objects.add(obj)
        return true
    }

    fun removeObject(x: Int, y: Int, obj: GameObject): Bool {
        return (getCell(x, y) ?: return false).objects.remove(obj)
    }

    fun objectsAt(x: Int, y: Int): List<GameObject> {
        return getCell(x, y)?.objects ?: emptyList()
    }

    fun moveObject(fromX: Int, fromY: Int, toX: Int, toY: Int, obj: GameObject): Bool {
        return if (!removeObject(fromX, fromY, obj)) false else placeObject(toX, toY, obj)
    }

    fun allObjects(): List<GameObject> {
        return cells.flatMap { it.objects }
    }
}

