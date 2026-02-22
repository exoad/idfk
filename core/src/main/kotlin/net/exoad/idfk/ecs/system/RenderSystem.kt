package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.ecs.component.*
import net.exoad.idfk.util.SpriteSheet
import net.exoad.idfk.world.Direction
import net.exoad.idfk.world.Direction.Companion.mapToTextureIndex

class RenderSystem(
    private val batch: SpriteBatch,
    private val font: BitmapFont,
) : IteratingSystem(allOf(PositionComponent::class).get()), DisposableSystem {

    private val positionMapper = mapperFor<PositionComponent>()
    private val textureMapper = mapperFor<TextureComponent>()
    private val sizeMapper = mapperFor<SizeComponent>()
    private val atlasMapper = mapperFor<AtlasComponent>()
    private val animationMapper = mapperFor<AnimationComponent>()
    private val directionMapper = mapperFor<DirectionComponent>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity[positionMapper]!!
        val sizeComp = entity[sizeMapper] ?: return
        val atlasComp = entity[atlasMapper]
        val textureComp = entity[textureMapper]
        when {
            atlasComp != null -> {
                val regions = SpriteSheet.regions(atlasComp.texturePath, atlasComp.frameWidth, atlasComp.frameHeight)
                val framesPerRow = SpriteSheet.texture(atlasComp.texturePath).width / atlasComp.frameWidth
                val animationComp = entity[animationMapper]
                val frameIndex = when {
                    animationComp != null ->
                        (entity[directionMapper]?.direction
                         ?: Direction.SOUTH).mapToTextureIndex() * framesPerRow + animationComp.frames[animationComp.currentFrame]

                    atlasComp.frameIndex != null -> atlasComp.frameIndex
                    else -> return
                }
                if (frameIndex in regions.indices) {
                    with(batch) {
                        color = Color.WHITE
                        draw(regions[frameIndex], position.x, position.y, sizeComp.width, sizeComp.height)
                    }
                }
            }

            textureComp != null -> {
                with(batch) {
                    color = Color.WHITE
                    draw(
                        SpriteSheet.texture(textureComp.texturePath),
                        position.x,
                        position.y,
                        sizeComp.width,
                        sizeComp.height
                    )
                }
            }
        }
    }

    override fun dispose() {
        font.dispose()
    }
}
