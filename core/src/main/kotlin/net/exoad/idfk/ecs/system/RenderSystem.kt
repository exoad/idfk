package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import ktx.assets.toInternalFile
import net.exoad.idfk.ecs.component.*

class RenderSystem(
    private val batch: SpriteBatch,
    private val font: BitmapFont
) :
    IteratingSystem(allOf(PositionComponent::class).get()) {
    private val positionMapper = mapperFor<PositionComponent>()
    private val textureMapper = mapperFor<TextureComponent>()
    private val sizeMapper = mapperFor<SizeComponent>()
    private val textMapper = mapperFor<TextComponent>()
    private val colorMapper = mapperFor<ColorComponent>()
    private val textureCache = mutableMapOf<String, Texture>()
    private var debugMode = false

    override fun update(deltaTime: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) && Gdx.input.isKeyJustPressed(
                Input.Keys.G
            )
        ) {
            debugMode = !debugMode
        }
        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity[positionMapper]!!
        val textureComp = entity[textureMapper]
        val sizeComp = entity[sizeMapper]
        val textComp = entity[textMapper]
        val colorComp = entity[colorMapper]
        if (textureComp != null && sizeComp != null) {
            val texturePath = if (debugMode) {
                "null.png"
            } else {
                textureComp.texturePath
            }
            val texture = textureCache.getOrPut(texturePath) {
                Texture(texturePath.toInternalFile())
            }
            batch.color = colorComp?.color ?: Color.WHITE
            batch.draw(
                texture,
                position.x,
                position.y,
                sizeComp.width,
                sizeComp.height
            )
            batch.color = Color.MAGENTA
        } else if (textComp != null) {
            font.color = colorComp?.color ?: Color.WHITE
            font.draw(batch, textComp.text, position.x, position.y)
            font.color = Color.MAGENTA
        }
    }

    fun dispose() {
        disposeTextures()
        font.dispose()
    }

    fun disposeTextures() {
        textureCache.values.forEach { it.dispose() }
    }
}
