package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.ecs.component.CollisionComponent
import net.exoad.idfk.ecs.component.PositionComponent
import net.exoad.idfk.ecs.component.TileMapComponent
import net.exoad.idfk.ecs.component.TileSetComponent
import net.exoad.idfk.util.SpriteRegistry
import net.exoad.idfk.util.SpriteSheet
import net.exoad.idfk.util.WorldObjectRegistry
import net.exoad.idfk.world.WorldManager
import kotlin.math.ceil
import kotlin.math.floor

class TileRenderSystem(
    private val batch: SpriteBatch,
    private val camera: OrthographicCamera,
    private val shapeRenderer: ShapeRenderer,
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
        val minX =
            (floor((camera.position.x - camera.viewportWidth / 2f - mapPos.x) / tileSz).toInt() - margin).coerceAtLeast(
                0
            )
        val maxX =
            (ceil((camera.position.x + camera.viewportWidth / 2f - mapPos.x) / tileSz).toInt() + margin).coerceAtMost(
                tileMap.width - 1
            )
        val minY =
            (floor((camera.position.y - camera.viewportHeight / 2f - mapPos.y) / tileSz).toInt() - margin).coerceAtLeast(
                0
            )
        val maxY =
            (ceil((camera.position.y + camera.viewportHeight / 2f - mapPos.y) / tileSz).toInt() + margin).coerceAtMost(
                tileMap.height - 1
            )
        val regions = SpriteSheet.regions(tileSet.tilesetPath, tileSet.tileSize, tileSet.tileSize)
        with(batch) {
            for (y in minY..maxY) {
                for (x in minX..maxX) {
                    val tileId = tileMap.tiles[y][x]
                    if (tileId in regions.indices) {
                        color = Color.WHITE
                        draw(
                            regions[tileId],
                            mapPos.x + x * tileSz,
                            mapPos.y + y * tileSz,
                            tileSz,
                            tileSz
                        )
                    }
                    // draw world objects placed on this tile
                    val objects = WorldManager["base"].objectGrid.getCell(x, y)?.objects ?: emptyList()
                    if (objects.isNotEmpty()) {
                        for (obj in objects) {
                            val objTypeInfo = WorldObjectRegistry.get(obj.type)
                            if (objTypeInfo != null) {
                                val spriteRegion = SpriteRegistry.getRegion(objTypeInfo.spriteName)
                                if (spriteRegion != null) {
                                    color = Color.WHITE
                                    draw(
                                        spriteRegion,
                                        mapPos.x + x * tileSz,
                                        mapPos.y + y * tileSz,
                                        tileSz,
                                        tileSz
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun drawCollisionBoxes() {
        val tileMapMapper = mapperFor<TileMapComponent>()
        val positionMapper = mapperFor<PositionComponent>()
        val tileSetMapper = mapperFor<TileSetComponent>()
        shapeRenderer.setProjectionMatrix(camera.combined)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = Color.MAGENTA
        for (entity in entities) {
            val tileMap = entity[tileMapMapper] ?: continue
            val mapPos = entity[positionMapper] ?: continue
            val tileSet = entity[tileSetMapper] ?: continue
            val tileSz = tileSet.tileSize.toFloat()
            val margin = 1
            val minX =
                (floor((camera.position.x - camera.viewportWidth / 2f - mapPos.x) / tileSz).toInt() - margin).coerceAtLeast(
                    0
                )
            val maxX =
                (ceil((camera.position.x + camera.viewportWidth / 2f - mapPos.x) / tileSz).toInt() + margin).coerceAtMost(
                    tileMap.width - 1
                )
            val minY =
                (floor((camera.position.y - camera.viewportHeight / 2f - mapPos.y) / tileSz).toInt() - margin).coerceAtLeast(
                    0
                )
            val maxY =
                (ceil((camera.position.y + camera.viewportHeight / 2f - mapPos.y) / tileSz).toInt() + margin).coerceAtMost(
                    tileMap.height - 1
                )
            for (y in minY..maxY) {
                for (x in minX..maxX) {
                    val objects = WorldManager["base"].objectGrid.getCell(x, y)?.objects ?: emptyList()
                    for (obj in objects) {
                        shapeRenderer.rect(
                            mapPos.x + x * tileSz + obj.offsetX,
                            mapPos.y + y * tileSz + obj.offsetY,
                            obj.width,
                            obj.height
                        )
                    }
                }
            }
        }
        shapeRenderer.end()
    }

    fun drawEntityCollisionBoxes(entities: Iterable<Entity>) {
        val positionMapper = mapperFor<PositionComponent>()
        val collisionMapper = mapperFor<CollisionComponent>()
        shapeRenderer.setProjectionMatrix(camera.combined)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = Color.MAGENTA
        for (entity in entities) {
            val position = entity[positionMapper] ?: continue
            val collision = entity[collisionMapper] ?: continue
            shapeRenderer.rect(
                position.x + collision.offsetX,
                position.y + collision.offsetY,
                collision.width,
                collision.height
            )
        }
        shapeRenderer.end()
    }

    override fun dispose() {
        SpriteSheet.dispose()
    }
}
