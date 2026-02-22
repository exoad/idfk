package net.exoad.idfk.util

import net.exoad.idfk.ecs.component.TileMapComponent

object TileMapLoader {
    fun createTileMap(
        width: Int,
        height: Int,
        tileSize: Int,
        tiles: Array<IntArray>,
        tileTextureMap: Map<Int, String> = mapOf(0 to "null.png")
    ): TileMapComponent {
        require(tiles.size == height) { "Tiles array height must match height parameter" }
        require(tiles.all { it.size == width }) { "All tile rows must have the same width" }
        return TileMapComponent(width, height, tileSize, tiles, tileTextureMap)
    }

    fun createTileMapFromString(
        mapString: String,
        tileSize: Int,
        tileTextureMap: Map<Int, String> = mapOf(0 to "null.png"),
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
        }.toTypedArray()
        val width = tiles.maxOfOrNull { it.size } ?: 0
        require(width > 0 && height > 0) { "Tilemap must have non-zero dimensions" }
        return TileMapComponent(width, height, tileSize, tiles, tileTextureMap)
    }
}

