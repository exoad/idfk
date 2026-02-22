package net.exoad.idfk.factories

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import net.exoad.idfk.ecs.component.*

object AtlasEntityFactory {

    fun createAnimatedEntity(
        engine: Engine,
        position: Vector2,
        texturePath: String,
        frameWidth: Int,
        frameHeight: Int,
        framesPerRow: Int,
        size: Vector2? = null,
        id: String = "animated-${System.nanoTime()}"
    ): Entity {
        val entitySize = size ?: Vector2(frameWidth.toFloat(), frameHeight.toFloat())
        val entity = Entity().apply {
            add(IdComponent(id))
            add(PositionComponent(position.x, position.y))
            add(SizeComponent(entitySize.x, entitySize.y))
            add(DirectionComponent())
            add(VelocityComponent(0f, 0f))
            add(AtlasFactory.createAnimatedAtlas(texturePath, frameWidth, frameHeight, framesPerRow))
            add(AtlasFactory.createIdleAnimation())
        }
        engine.addEntity(entity)
        return entity
    }

    fun createStaticSpriteEntity(
        engine: Engine,
        position: Vector2,
        size: Vector2,
        texturePath: String,
        frameWidth: Int,
        frameHeight: Int,
        framesPerRow: Int,
        frameIndex: Int,
        id: String = "sprite-${System.nanoTime()}"
    ): Entity {
        val entity = Entity().apply {
            add(IdComponent(id))
            add(PositionComponent(position.x, position.y))
            add(SizeComponent(size.x, size.y))
            add(AtlasFactory.createStaticAtlas(texturePath, frameWidth, frameHeight, framesPerRow, frameIndex))
        }
        engine.addEntity(entity)
        return entity
    }

    fun createUIAtlasElement(
        engine: Engine,
        position: Vector2,
        size: Vector2,
        texturePath: String,
        frameWidth: Int,
        frameHeight: Int,
        framesPerRow: Int,
        frameIndex: Int,
        id: String = "ui-${System.nanoTime()}"
    ): Entity {
        val entity = Entity().apply {
            add(IdComponent(id))
            add(PositionComponent(position.x, position.y))
            add(SizeComponent(size.x, size.y))
            add(AtlasFactory.createStaticAtlas(texturePath, frameWidth, frameHeight, framesPerRow, frameIndex))
        }
        engine.addEntity(entity)
        return entity
    }

}
