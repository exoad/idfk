package net.exoad.idfk.factories

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import net.exoad.idfk.ecs.component.PositionComponent
import net.exoad.idfk.ecs.component.TileMapComponent
import net.exoad.idfk.ecs.component.TileSetComponent
import net.exoad.idfk.util.TileMapLoader

object TileMapFactory {
    fun createTileMap(
        engine: Engine,
        position: Vector2,
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
        position: Vector2,
        mapString: String,
        tileSize: Int,
        tileTextureMap: Map<Int, String> = mapOf(0 to "null.png")
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
        position: Vector2,
        mapString: String,
        tileSize: Int,
        tilesetPath: String,
        tilesPerRow: Int
    ) {
        val tileMapComponent = TileMapLoader.createTileMapFromString(
            mapString,
            tileSize,
            mapOf()
        )
        val tileSetComponent = TileSetComponent(tilesetPath, tileSize, tilesPerRow)
        createTileMap(engine, position, tileMapComponent, tileSetComponent)
    }
}

