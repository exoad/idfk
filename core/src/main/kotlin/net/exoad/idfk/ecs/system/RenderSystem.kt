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
import net.exoad.idfk.ecs.component.*
import net.exoad.idfk.util.Logger
import net.exoad.idfk.world.Direction

class RenderSystem(
    private val batch: SpriteBatch,
    private val font: BitmapFont,
    var camera: OrthographicCamera
) :
    IteratingSystem(allOf(PositionComponent::class).get()) {

    private val positionMapper = mapperFor<PositionComponent>()
    private val textureMapper = mapperFor<TextureComponent>()
    private val sizeMapper = mapperFor<SizeComponent>()
    private val atlasMapper = mapperFor<AtlasComponent>()
    private val animationMapper = mapperFor<AnimationComponent>()
    private val textureCache = mutableMapOf<String, Texture>()
    private val textureRegionCache = mutableMapOf<String, Array<TextureRegion>>()

    override fun update(deltaTime: Float) {
        val players =
            engine.getEntitiesFor(allOf(PlayerComponent::class, PositionComponent::class, SizeComponent::class).get())
        if (players.size() > 0) {
            val p = players.first()
            val pos = p[positionMapper]!!
            val size = p[sizeMapper]!!
            val centerX = pos.x + size.width / 2f
            val centerY = pos.y + size.height / 2f
            val scale = Shared.VISUAL_SCALE
            camera.position.set(centerX / scale, centerY / scale, 0f)
            Logger.info("Camera set to: ($centerX, $centerY), Player at: (${pos.x}, ${pos.y})")
        }
        camera.update()
        batch.projectionMatrix = camera.combined
        Logger.info("Camera bounds: left=${camera.position.x - camera.viewportWidth / 2}, right=${camera.position.x + camera.viewportWidth / 2}, bottom=${camera.position.y - camera.viewportHeight / 2}, top=${camera.position.y + camera.viewportHeight / 2}")
        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity[positionMapper]!!
        val textureComp = entity[textureMapper]
        val sizeComp = entity[sizeMapper]
        val atlasComp = entity[atlasMapper]
        val animationComp = entity[animationMapper]
        val scale = Shared.VISUAL_SCALE
        val worldX = position.x / scale
        val worldY = position.y / scale
        when {
            atlasComp != null && sizeComp != null -> {
                if (animationComp != null) {
                    renderAnimatedAtlas(
                        entity,
                        batch,
                        atlasComp,
                        animationComp,
                        PositionComponent(worldX, worldY),
                        SizeComponent(sizeComp.width / scale, sizeComp.height / scale),
                        Color.WHITE
                    )
                } else if (atlasComp.frameIndex != null) {
                    renderStaticAtlas(
                        batch,
                        atlasComp,
                        atlasComp.frameIndex,
                        PositionComponent(worldX, worldY),
                        SizeComponent(sizeComp.width / scale, sizeComp.height / scale),
                        Color.WHITE
                    )
                }
            }

            textureComp != null && sizeComp != null -> {
                with(batch) {
                    color = Color.WHITE
                    draw(
                        textureCache.getOrPut(textureComp.texturePath) {
                            Texture(textureComp.texturePath.toInternalFile())
                        },
                        worldX,
                        worldY,
                        sizeComp.width / scale,
                        sizeComp.height / scale
                    )
                    color = Color.WHITE
                }
            }
        }
    }

    private fun fetchRegions(atlas: AtlasComponent): Array<TextureRegion> {
        return textureRegionCache.getOrPut(atlas.texturePath) {
            val texture = textureCache.getOrPut(atlas.texturePath) {
                Texture(atlas.texturePath.toInternalFile())
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
        size: SizeComponent,
        initialColor: Color
    ) {
        val regions = fetchRegions(atlas)
        val frameIndex = (when (entity[mapperFor<DirectionComponent>()]?.direction ?: Direction.SOUTH) {
                              Direction.SOUTH -> 0
                              Direction.WEST -> 1
                              Direction.EAST -> 2
                              Direction.NORTH -> 3
                          } * atlas.framesPerRow) + animation.frames[animation.currentFrame]
        if (frameIndex >= 0 && frameIndex < regions.size) {
            with(batch) {
                color = initialColor
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
        size: SizeComponent,
        initialColor: Color
    ) {
        val regions = fetchRegions(atlas)
        if (frameIndex >= 0 && frameIndex < regions.size) {
            with(batch) {
                color = initialColor
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
