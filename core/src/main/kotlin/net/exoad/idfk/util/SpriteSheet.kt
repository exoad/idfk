package net.exoad.idfk.util

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.assets.toInternalFile
import net.exoad.idfk.Str

object SpriteSheet {
    private data class Key(val path: Str, val frameWidth: Int, val frameHeight: Int)

    private val textures = mutableMapOf<Str, Texture>()
    private val regions = mutableMapOf<Key, Array<TextureRegion>>()

    fun regions(path: Str, frameWidth: Int, frameHeight: Int): Array<TextureRegion> {
        return regions.getOrPut(Key(path, frameWidth, frameHeight)) {
            val tex = texture(path)
            val cols = tex.width / frameWidth
            val padding = 0.5f
            Array(cols * (tex.height / frameHeight)) { i ->
                TextureRegion(
                    tex,
                    ((i % cols) * frameWidth + padding).toInt(),
                    ((i / cols) * frameHeight + padding).toInt(),
                    (frameWidth - 2 * padding).toInt(),
                    (frameHeight - 2 * padding).toInt()
                )
            }
        }
    }

    fun texture(path: Str): Texture {
        return textures.getOrPut(path) {
            Texture(path.toInternalFile()).also {
                it.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
            }
        }
    }

    fun dispose() {
        textures.values.forEach {
            it.dispose()
        }
        textures.clear()
        regions.clear()
    }
}

