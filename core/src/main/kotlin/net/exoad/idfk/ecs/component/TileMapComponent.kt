package net.exoad.idfk.ecs.component

import com.badlogic.ashley.core.Component
import net.exoad.idfk.Bool
import net.exoad.idfk.Str

data class TileMapComponent(
    val width: Int,
    val height: Int,
    val tileSize: Int,
    val tiles: Array<IntArray>,
    val tileTextureMap: Map<Int, Str> = mapOf(0 to "null.png")
) : Component {
    override fun equals(other: Any?): Bool {
        return this === other
               || (other is TileMapComponent
                   && width == other.width
                   && height == other.height
                   && tileSize == other.tileSize
                   && tiles.contentDeepEquals(other.tiles)
                   && tileTextureMap == other.tileTextureMap)
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

