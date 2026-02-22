package net.exoad.idfk.factories

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import net.exoad.idfk.Str
import net.exoad.idfk.Vec2
import net.exoad.idfk.ecs.component.PositionComponent
import net.exoad.idfk.ecs.component.TileMapComponent
import net.exoad.idfk.ecs.component.TileSetComponent
import net.exoad.idfk.util.TileMapLoader

object TileMapFactory {
    fun createTileMap(
        engine: Engine,
        position: Vec2,
        tileMapComponent: TileMapComponent,
        tileSetComponent: TileSetComponent? = null
    ) {
        engine.addEntity(
            Entity().apply {
                add(PositionComponent(position.x, position.y))
                add(tileMapComponent)
                if (tileSetComponent != null) {
                    add(tileSetComponent)
                }
            }
        )
    }

    fun createTileMapFromString(
        engine: Engine,
        position: Vec2,
        mapString: Str,
        tileSize: Int,
        tileTextureMap: Map<Int, Str> = mapOf(0 to "null.png")
    ) {
        createTileMap(
            engine,
            position,
            TileMapLoader.createTileMapFromString(
                mapString,
                tileSize,
                tileTextureMap
            )
        )
    }

    fun createTileMapWithTileSet(
        engine: Engine,
        position: Vec2,
        mapString: Str,
        tileSize: Int,
        tilesetPath: Str,
        tilesPerRow: Int
    ) {
        createTileMap(
            engine,
            position,
            TileMapLoader.createTileMapFromString(
                mapString,
                tileSize,
                mapOf()
            ),
            TileSetComponent(
                tilesetPath,
                tileSize,
                tilesPerRow
            )
        )
    }
}

