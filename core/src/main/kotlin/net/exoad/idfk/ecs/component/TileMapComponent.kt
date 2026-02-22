package net.exoad.idfk.ecs.component

import com.badlogic.ashley.core.Component

data class TileMapComponent(
    val width: Int,
    val height: Int,
    val tileSize: Int,
    val tiles: Array<IntArray>,
    val tileTextureMap: Map<Int, String> = mapOf(
        0 to "null.png"
    )
) : Component {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TileMapComponent) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (tileSize != other.tileSize) return false
        if (!tiles.contentDeepEquals(other.tiles)) return false
        if (tileTextureMap != other.tileTextureMap) return false
        return true
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        result = 31 * result + tileSize
        result = 31 * result + tiles.contentDeepHashCode()
        result = 31 * result + tileTextureMap.hashCode()
        return result
    }
}

