package net.exoad.idfk.util

data class TileCell(var groundTileId: Int = 0, val objects: MutableList<WorldObject> = mutableListOf())
