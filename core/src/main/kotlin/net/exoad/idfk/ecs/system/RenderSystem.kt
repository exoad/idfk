package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import ktx.assets.toInternalFile
import net.exoad.idfk.Str
import net.exoad.idfk.ecs.component.*
import net.exoad.idfk.world.Direction
import net.exoad.idfk.world.Direction.Companion.mapToTextureIndex

class RenderSystem(
    private val batch: SpriteBatch,
    private val font: BitmapFont,
) : IteratingSystem(allOf(PositionComponent::class).get()) {

    private val positionMapper = mapperFor<PositionComponent>()
    private val textureMapper = mapperFor<TextureComponent>()
    private val sizeMapper = mapperFor<SizeComponent>()
    private val atlasMapper = mapperFor<AtlasComponent>()
    private val animationMapper = mapperFor<AnimationComponent>()
    private val textureCache = mutableMapOf<Str, Texture>()
    private val textureRegionCache = mutableMapOf<Str, Array<TextureRegion>>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity[positionMapper]!!
        val textureComp = entity[textureMapper]
        val sizeComp = entity[sizeMapper]
        val atlasComp = entity[atlasMapper]
        val animationComp = entity[animationMapper]
        val worldX = position.x
        val worldY = position.y
        when {
            atlasComp != null && sizeComp != null -> {
                if (animationComp != null) {
                    renderAnimatedAtlas(
                        entity,
                        batch,
                        atlasComp,
                        animationComp,
                        PositionComponent(worldX, worldY),
                        SizeComponent(sizeComp.width, sizeComp.height)
                    )
                } else if (atlasComp.frameIndex != null) {
                    renderStaticAtlas(
                        batch,
                        atlasComp,
                        atlasComp.frameIndex,
                        PositionComponent(worldX, worldY),
                        SizeComponent(sizeComp.width, sizeComp.height)
                    )
                }
            }

            textureComp != null && sizeComp != null -> {
                with(batch) {
                    color = Color.WHITE
                    draw(
                        textureCache.getOrPut(textureComp.texturePath) {
                            Texture(textureComp.texturePath.toInternalFile()).apply {
                                setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
                            }
                        },
                        worldX,
                        worldY,
                        sizeComp.width,
                        sizeComp.height
                    )
                    color = Color.WHITE
                }
            }
        }
    }

    private fun fetchRegions(atlas: AtlasComponent): Array<TextureRegion> {
        return textureRegionCache.getOrPut(atlas.texturePath) {
            val texture = textureCache.getOrPut(atlas.texturePath) {
                Texture(atlas.texturePath.toInternalFile()).apply {
                    setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
                }
            }
            val regions = mutableListOf<TextureRegion>()
            val framesPerRow = atlas.framesPerRow
            with(regions) {
                for (y in 0 until (texture.height / atlas.frameHeight)) {
                    for (x in 0 until framesPerRow) {
                        add(
                            TextureRegion(
                                texture,
                                x * atlas.frameWidth,
                                y * atlas.frameHeight,
                                atlas.frameWidth,
                                atlas.frameHeight
                            )
                        )
                    }
                }
                toTypedArray()
            }
        }
    }

    private fun renderAnimatedAtlas(
        entity: Entity,
        batch: SpriteBatch,
        atlas: AtlasComponent,
        animation: AnimationComponent,
        position: PositionComponent,
        size: SizeComponent
    ) {
        val regions = fetchRegions(atlas)
        val frameIndex = ((entity[mapperFor<DirectionComponent>()]?.direction ?: Direction.SOUTH).mapToTextureIndex()
                          * atlas.framesPerRow) + animation.frames[animation.currentFrame]
        if (frameIndex >= 0 && frameIndex < regions.size) {
            with(batch) {
                color = Color.WHITE
                draw(
                    regions[frameIndex],
                    position.x,
                    position.y,
                    size.width,
                    size.height
                )
                color = Color.WHITE
            }
        }
    }

    private fun renderStaticAtlas(
        batch: SpriteBatch,
        atlas: AtlasComponent,
        frameIndex: Int,
        position: PositionComponent,
        size: SizeComponent
    ) {
        val regions = fetchRegions(atlas)
        if (frameIndex >= 0 && frameIndex < regions.size) {
            with(batch) {
                color = Color.WHITE
                draw(
                    regions[frameIndex],
                    position.x,
                    position.y,
                    size.width,
                    size.height
                )
                color = Color.WHITE
            }
        }
    }

    fun dispose() {
        disposeTextures()
        font.dispose()
    }

    fun disposeTextures() {
        textureCache.values.forEach {
            it.dispose()
        }
    }
}
