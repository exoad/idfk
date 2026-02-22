package net.exoad.idfk.util

import com.badlogic.gdx.graphics.g2d.TextureRegion
import net.exoad.idfk.Str

/**
 * SpriteRegistry
 *
 * Purpose:
 * - Define sprite sheets (path + cell size).
 * - Define named sprites by grid coordinates (gridX, gridY) relative to a sheet.
 * - Define automatic sequences (auto-incrementing indices) across a sheet.
 * - Resolve a named sprite to a TextureRegion via SpriteSheet.
 */
object SpriteRegistry {

    data class Sheet(val key: Str, val path: Str, val frameWidth: Int, val frameHeight: Int)
    private data class SpriteRef(val name: Str, val sheetKey: Str, val frameIndex: Int, val cellWidth: Int = 1, val cellHeight: Int = 1)

    private val sheets = mutableMapOf<Str, Sheet>()
    private val sprites = mutableMapOf<Str, SpriteRef>()

    fun defineSheet(key: Str, path: Str, frameWidth: Int, frameHeight: Int) {
        sheets[key] = Sheet(key, path, frameWidth, frameHeight)
    }

    fun defineSpriteByGrid(name: Str, sheetKey: Str, gridX: Int, gridY: Int, cellWidth: Int = 1, cellHeight: Int = 1) {
        val sheet = sheets[sheetKey] ?: throw IllegalArgumentException("Sheet '$sheetKey' not found. Call defineSheet first.")
        val cols = SpriteSheet.texture(sheet.path).width / sheet.frameWidth
        val frameIndex = gridY * cols + gridX
        sprites[name] = SpriteRef(name, sheetKey, frameIndex, cellWidth, cellHeight)
    }

    fun defineGridSequence(sheetKey: Str, startGridX: Int, startGridY: Int, count: Int, namePrefix: Str): List<Str> {
        val sheet = sheets[sheetKey] ?: throw IllegalArgumentException("Sheet '$sheetKey' not found. Call defineSheet first.")
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
        val sheet = sheets[sheetKey] ?: throw IllegalArgumentException("Sheet '$sheetKey' not found. Call defineSheet first.")
        val tex = SpriteSheet.texture(sheet.path)
        val cols = tex.width / sheet.frameWidth
        val names = mutableListOf<Str>()
        for ((i, coord) in coords.withIndex()) {
            val (gx, gy) = coord
            val nm = "$namePrefix$i"
            val idx = gy * cols + gx
            sprites[nm] = SpriteRef(nm, sheetKey, idx)
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

    fun getFrameIndex(name: Str): Int? = sprites[name]?.frameIndex

    fun getSheetPath(name: Str): Str? = sprites[name]?.sheetKey?.let { sheets[it]?.path }

    // New API: resolve a sheet path by its sheet key
    fun getSheetPathByKey(sheetKey: Str): Str? = sheets[sheetKey]?.path

    fun clear() {
        sheets.clear()
        sprites.clear()
    }

}
