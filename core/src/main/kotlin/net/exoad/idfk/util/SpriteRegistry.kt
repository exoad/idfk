package net.exoad.idfk.util

import com.badlogic.gdx.graphics.g2d.TextureRegion
import net.exoad.idfk.Str
import net.exoad.idfk.Vec2

object SpriteRegistry {
    data class Sheet(val key: Str, val path: Str, val frameWidth: Int, val frameHeight: Int)

    private data class SpriteRef(
        val name: Str,
        val sheetKey: Str,
        val frameIndex: Int,
        val cellWidth: Int = 1,
        val cellHeight: Int = 1
    )

    data class CollisionRect(val offsetX: Float, val offsetY: Float, val width: Float, val height: Float)

    private val sheets = mutableMapOf<Str, Sheet>()
    private val sprites = mutableMapOf<Str, SpriteRef>()
    private val collisionRects = mutableMapOf<Str, CollisionRect>()

    class SheetBuilder(val key: Str, val path: Str, val frameWidth: Int, val frameHeight: Int) {
        private val localSprites = mutableListOf<Pair<String, () -> Unit>>()

        fun sprite(
            name: Str,
            gridX: Int,
            gridY: Int,
            cellWidth: Int = 1,
            cellHeight: Int = 1,
            collision: (CollisionBuilder.() -> Unit)? = null
        ) {
            localSprites.add(name to {
                defineSpriteByGrid(name, key, gridX, gridY, cellWidth, cellHeight)
                if (collision != null) {
                    val builder = CollisionBuilder()
                    builder.collision()
                    defineCollisionRectConverted(
                        name,
                        Vec2(builder.offsetX, builder.offsetY),
                        Vec2(builder.width, builder.height),
                        frameHeight.toFloat()
                    )
                }
            })
        }

        fun sequence(startGridX: Int, startGridY: Int, count: Int, namePrefix: Str) {
            localSprites.add("$namePrefix*" to {
                defineGridSequence(key, startGridX, startGridY, count, namePrefix)
            })
        }

        internal fun build() {
            defineSheet(key, path, frameWidth, frameHeight)
            localSprites.forEach { (_, action) -> action() }
        }
    }

    data class CollisionBuilder(
        var offsetX: Float = 0f,
        var offsetY: Float = 0f,
        var width: Float = 0f,
        var height: Float = 0f
    )

    fun sheet(key: Str, path: Str, frameWidth: Int, frameHeight: Int, block: SheetBuilder.() -> Unit) {
        with(SheetBuilder(key, path, frameWidth, frameHeight)) {
            block()
            build()
        }
    }

    fun defineSheet(key: Str, path: Str, frameWidth: Int, frameHeight: Int) {
        sheets[key] = Sheet(key, path, frameWidth, frameHeight)
    }

    fun defineSpriteByGrid(name: Str, sheetKey: Str, gridX: Int, gridY: Int, cellWidth: Int = 1, cellHeight: Int = 1) {
        val sheet =
            sheets[sheetKey] ?: throw IllegalArgumentException("Sheet '$sheetKey' not found. Call defineSheet first.")
        sprites[name] = SpriteRef(
            name,
            sheetKey,
            gridY * (SpriteSheet.texture(sheet.path).width / sheet.frameWidth) + gridX,
            cellWidth,
            cellHeight
        )
    }

    fun defineGridSequence(sheetKey: Str, startGridX: Int, startGridY: Int, count: Int, namePrefix: Str): List<Str> {
        val sheet =
            sheets[sheetKey] ?: throw IllegalArgumentException("Sheet '$sheetKey' not found. Call defineSheet first.")
        if (count <= 0) return emptyList()
        val tex = SpriteSheet.texture(sheet.path)
        val cols = tex.width / sheet.frameWidth
        val names = mutableListOf<Str>()
        var index = startGridY * cols + startGridX
        for (i in 0 until count) {
            val nm = "$namePrefix$i"
            sprites[nm] = SpriteRef(nm, sheetKey, index)
            names += nm
            index++
        }
        return names
    }

    fun defineSpritesByCoords(sheetKey: Str, coords: List<Pair<Int, Int>>, namePrefix: Str): List<Str> {
        val sheet =
            sheets[sheetKey] ?: throw IllegalArgumentException("Sheet '$sheetKey' not found. Call defineSheet first.")
        val tex = SpriteSheet.texture(sheet.path)
        val cols = tex.width / sheet.frameWidth
        val names = mutableListOf<Str>()
        for ((i, coord) in coords.withIndex()) {
            val nm = "$namePrefix$i"
            sprites[nm] = SpriteRef(nm, sheetKey, coord.second * cols + coord.first)
            names += nm
        }
        return names
    }

    fun getRegion(name: Str): TextureRegion? {
        val ref = sprites[name] ?: return null
        val sheet = sheets[ref.sheetKey] ?: return null
        val regions = SpriteSheet.regions(sheet.path, sheet.frameWidth, sheet.frameHeight)
        return if (ref.frameIndex in regions.indices) regions[ref.frameIndex] else null
    }

    fun getFrameIndex(name: Str): Int {
        return sprites[name]?.frameIndex
               ?: throw IllegalArgumentException("Sprite '$name' not found. Call defineSpriteByGrid or defineSpritesByCoords first.")
    }

    fun getSheetPath(name: Str): Str {
        return sprites[name]?.sheetKey?.let { sheets[it]?.path }
               ?: throw IllegalArgumentException("Sprite '$name' not found. Call defineSpriteByGrid or defineSpritesByCoords first.")
    }

    fun getSheetPathByKey(sheetKey: Str): Str {
        return sheets[sheetKey]?.path
               ?: throw IllegalArgumentException("Sheet '$sheetKey' not found. Call defineSheet first.")
    }

    fun defineCollisionRect(name: Str, offset: Vec2, dimension: Vec2) {
        collisionRects[name] = CollisionRect(offset.x, offset.y, dimension.x, dimension.y)
    }

    fun defineCollisionRectConverted(
        name: Str,
        offset: Vec2,
        dimension: Vec2,
        spriteHeight: Float
    ) {
        collisionRects[name] = CollisionRectConverter.convertFromTopLeft(offset, dimension, spriteHeight)
    }

    fun getCollisionRect(name: Str): CollisionRect {
        return collisionRects[name]
               ?: throw IllegalArgumentException("Collision rect for '$name' not found. Call defineCollisionRect first.")
    }

    fun clear() {
        sheets.clear()
        sprites.clear()
        collisionRects.clear()
    }

}
