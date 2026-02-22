package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import ktx.assets.toInternalFile
import net.exoad.idfk.Shared
import net.exoad.idfk.ecs.component.PositionComponent
import net.exoad.idfk.ecs.component.TileMapComponent
import net.exoad.idfk.ecs.component.TileSetComponent
import kotlin.math.ceil
import kotlin.math.floor

class TileRenderSystem(
    private val batch: SpriteBatch,
    private val camera: OrthographicCamera,
    private val debugFont: BitmapFont? = null
) : IteratingSystem(allOf(TileMapComponent::class, PositionComponent::class).get()) {

    private val tileMapMapper = mapperFor<TileMapComponent>()
    private val tileSetMapper = mapperFor<TileSetComponent>()
    private val positionMapper = mapperFor<PositionComponent>()
    private val textureCache = mutableMapOf<String, Texture>()
    private val tileSetRegionCache = mutableMapOf<String, Array<TextureRegion>>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val tileMap = entity[tileMapMapper]!!
        val mapPosition = entity[positionMapper]!!
        val tileSet = entity[tileSetMapper]
        val camLeft = camera.position.x - camera.viewportWidth / 2f
        val camRight = camera.position.x + camera.viewportWidth / 2f
        val camBottom = camera.position.y - camera.viewportHeight / 2f
        val camTop = camera.position.y + camera.viewportHeight / 2f
        val camLeftPx = camLeft * Shared.VISUAL_SCALE
        val camRightPx = camRight * Shared.VISUAL_SCALE
        val camBottomPx = camBottom * Shared.VISUAL_SCALE
        val camTopPx = camTop * Shared.VISUAL_SCALE
        val tileSizePx = tileMap.tileSize.toFloat()
        val marginTiles = 1
        val minX = (floor((camLeftPx - mapPosition.x) / tileSizePx).toInt() - marginTiles).coerceAtLeast(0)
        val maxX =
            (ceil((camRightPx - mapPosition.x) / tileSizePx).toInt() + marginTiles).coerceAtMost(tileMap.width - 1)
        val minY = (floor((camBottomPx - mapPosition.y) / tileSizePx).toInt() - marginTiles).coerceAtLeast(0)
        val maxY =
            (ceil((camTopPx - mapPosition.y) / tileSizePx).toInt() + marginTiles).coerceAtMost(tileMap.height - 1)
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val tileId = tileMap.tiles[y][x]
                val worldX = (mapPosition.x + (x * tileMap.tileSize)) / Shared.VISUAL_SCALE
                val worldY = (mapPosition.y + (y * tileMap.tileSize)) / Shared.VISUAL_SCALE
                val worldTileSize = tileMap.tileSize.toFloat() / Shared.VISUAL_SCALE
                if (tileSet != null) {
                    renderTileFromTileSet(batch, tileSet, tileId, worldX, worldY, worldTileSize)
                } else {
                    renderTileFromTextureMap(batch, tileMap, tileId, worldX, worldY, worldTileSize)
                }
                if (Shared.DEBUG) {
                    if (debugFont != null) {
                        debugFont.color = Color.WHITE
                        debugFont.draw(
                            batch, tileId.toString(), worldX + 2f / Shared.VISUAL_SCALE, worldY + worldTileSize - 2f /
                                                                                         Shared.VISUAL_SCALE
                        )
                    }
                }
            }
        }
    }

    private fun renderTileFromTileSet(
        batch: SpriteBatch,
        tileSet: TileSetComponent,
        tileId: Int,
        x: Float,
        y: Float,
        worldTileSize: Float
    ) {
        val regions = tileSetRegionCache.getOrPut(tileSet.tilesetPath) {
            val texture = textureCache.getOrPut(tileSet.tilesetPath) {
                Texture(tileSet.tilesetPath.toInternalFile())
            }
            val regions = mutableListOf<TextureRegion>()
            val tilesPerRow = tileSet.tilesPerRow
            val numRows = texture.height / tileSet.tileSize
            for (row in 0 until numRows) {
                for (col in 0 until tilesPerRow) {
                    regions.add(
                        TextureRegion(
                            texture,
                            col * tileSet.tileSize,
                            row * tileSet.tileSize,
                            tileSet.tileSize,
                            tileSet.tileSize
                        )
                    )
                }
            }
            regions.toTypedArray()
        }
        if (tileId >= 0 && tileId < regions.size) {
            batch.color = Color.WHITE
            batch.draw(regions[tileId], x, y, worldTileSize, worldTileSize)
        }
    }

    private fun renderTileFromTextureMap(
        batch: SpriteBatch,
        tileMap: TileMapComponent,
        tileId: Int,
        x: Float,
        y: Float,
        worldTileSize: Float
    ) {
        val texturePath = tileMap.tileTextureMap[tileId] ?: tileMap.tileTextureMap[0] ?: "null.png"
        batch.color = Color.WHITE
        batch.draw(
            textureCache.getOrPut(texturePath) {
                Texture(texturePath.toInternalFile())
            },
            x,
            y,
            worldTileSize,
            worldTileSize
        )
    }

    fun disposeTextures() {
        with(textureCache) {
            values.forEach { it.dispose() }
            clear()
        }
        tileSetRegionCache.clear()
    }
}
