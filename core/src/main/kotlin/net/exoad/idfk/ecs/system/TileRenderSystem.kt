package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.ecs.component.PositionComponent
import net.exoad.idfk.ecs.component.TileMapComponent
import net.exoad.idfk.ecs.component.TileSetComponent
import net.exoad.idfk.util.SpriteSheet
import kotlin.math.ceil
import kotlin.math.floor

class TileRenderSystem(
    private val batch: SpriteBatch,
    private val camera: OrthographicCamera,
) : IteratingSystem(
    allOf(
        TileMapComponent::class,
        PositionComponent::class,
        TileSetComponent::class
    ).get()
), DisposableSystem {

    private val tileMapMapper = mapperFor<TileMapComponent>()
    private val tileSetMapper = mapperFor<TileSetComponent>()
    private val positionMapper = mapperFor<PositionComponent>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val tileMap = entity[tileMapMapper]!!
        val mapPos = entity[positionMapper]!!
        val tileSet = entity[tileSetMapper]!!
        val tileSz = tileSet.tileSize.toFloat()
        val margin = 1
        val minX = (floor((camera.position.x - camera.viewportWidth / 2f - mapPos.x) / tileSz).toInt() - margin).coerceAtLeast(0)
        val maxX =
            (ceil((camera.position.x + camera.viewportWidth / 2f - mapPos.x) / tileSz).toInt() + margin).coerceAtMost(tileMap.width - 1)
        val minY = (floor((camera.position.y - camera.viewportHeight / 2f - mapPos.y) / tileSz).toInt() - margin).coerceAtLeast(0)
        val maxY =
            (ceil((camera.position.y + camera.viewportHeight / 2f - mapPos.y) / tileSz).toInt() + margin).coerceAtMost(tileMap.height - 1)
        val regions = SpriteSheet.regions(
            tileSet.tilesetPath,
            tileSet.tileSize,
            tileSet.tileSize
        )
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val tileId = tileMap.tiles[y][x]
                if (tileId in regions.indices) {
                    batch.color = Color.WHITE
                    batch.draw(
                        regions[tileId],
                        // avoid subpixel rendering artifacts by rounding the position to the nearest pixel
                        // cause neighboring tiles to bleed into each other from the main tile sheet texture
                        (mapPos.x + x * tileSz).toInt().toFloat(),
                        (mapPos.y + y * tileSz).toInt().toFloat(),
                        tileSz,
                        tileSz
                    )
                }
            }
        }
    }

    override fun dispose() {
        SpriteSheet.dispose()
    }
}
