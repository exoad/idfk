package net.exoad.idfk.world

import net.exoad.idfk.Str
import net.exoad.idfk.ecs.component.TileSetComponent
import net.exoad.idfk.util.TileGrid
import net.exoad.idfk.util.TileMapLoader
import net.exoad.idfk.util.WorldObjectRegistry
import kotlin.random.Random

object WorldGenerator {
    private val rng = Random(System.currentTimeMillis())

    fun generateWorldAsString(width: Int, height: Int, textureIndices: IntArray): Str {
        return buildString {
            for (y in 0 until height) {
                for (x in 0 until width) {
                    append(textureIndices[rng.nextInt(textureIndices.size)])
                    if (x < width - 1) {
                        append(" ")
                    }
                }
                if (y < height - 1) {
                    appendLine()
                }
            }
        }
    }

    fun generateWorld(
        width: Int,
        height: Int,
        textureIndices: IntArray,
        spawnX: Float,
        spawnY: Float,
        tileSetComponent: TileSetComponent,
        name: String,
        tileSize: Int = 16
    ): World {
        val objectGrid = TileGrid(width, height)
        val baseSeed = System.currentTimeMillis()
        val treePositions = NoiseBasedWorldGenerator.generateTreePositions(
            width = width,
            height = height,
            treeDensity = 0.15f,
            seed = baseSeed
        )
        for ((treeX, treeY) in treePositions) {
            WorldObjectRegistry.instantiate("tree")?.let {
                objectGrid.placeObject(treeX, treeY, it)
            }
        }
        val signPosts = NoiseBasedWorldGenerator.generateObjectPositionsWithSeed(
            width = width,
            height = height,
            density = 0.12f,
            scale = 0.08f,
            seed = baseSeed,
            objectType = "signPosts"
        )
        for ((stoneX, stoneY) in signPosts) {
            if (objectGrid.getCell(stoneX, stoneY)?.objects?.isEmpty() != false) {
                WorldObjectRegistry.instantiate("signPosts")?.let {
                    objectGrid.placeObject(stoneX, stoneY, it)
                }
            }
        }
        objectGrid.removeObject(spawnX.toInt(), spawnY.toInt())
        return World(
            name = name,
            tileMapComponent = TileMapLoader.createTileMapFromString(
                generateWorldAsString(width, height, textureIndices),
                tileSize = tileSize
            ),
            tileSetComponent = tileSetComponent,
            objectGrid = objectGrid,
            spawnX = spawnX,
            spawnY = spawnY
        )
    }
}
