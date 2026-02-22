package net.exoad.idfk.util

import net.exoad.idfk.Str
import net.exoad.idfk.ecs.component.TileMapComponent

object TileMapLoader {
    fun createTileMapFromString(
        mapString: Str,
        tileSize: Int,
        separator: Regex = Regex("[\\s,]+")
    ): TileMapComponent {
        val lines = mapString.trim().split("\n").filter { it.isNotBlank() }
        val height = lines.size
        val tiles = lines.map { line ->
            line.trim()
                .split(separator)
                .filter { it.isNotBlank() }
                .map { it.toInt() }
                .toIntArray()
        }
        val width = tiles.maxOfOrNull { it.size } ?: 0
        return TileMapComponent(
            width,
            height,
            tileSize,
            tiles.map {
                if (it.size < width) {
                    it + IntArray(width - it.size)
                } else {
                    it
                }
            }.toTypedArray<IntArray>()
        )
    }
}

