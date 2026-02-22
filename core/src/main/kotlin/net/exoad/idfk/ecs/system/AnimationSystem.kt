package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.ecs.component.AnimationComponent
import net.exoad.idfk.ecs.component.DirectionComponent
import net.exoad.idfk.ecs.component.VelocityComponent
import net.exoad.idfk.world.Direction
import kotlin.math.absoluteValue

class AnimationSystem :
    IteratingSystem(allOf(AnimationComponent::class, VelocityComponent::class, DirectionComponent::class).get()) {

    private val animationMapper = mapperFor<AnimationComponent>()
    private val velocityMapper = mapperFor<VelocityComponent>()
    private val directionMapper = mapperFor<DirectionComponent>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val animation = entity[animationMapper]!!
        val velocity = entity[velocityMapper]!!
        val dirComponent = entity[directionMapper]!!
        val isMoving = velocity.x != 0f || velocity.y != 0f
        if (isMoving) {
            dirComponent.direction = when {
                velocity.x.absoluteValue >= velocity.y.absoluteValue -> if (velocity.x > 0) Direction.EAST else Direction.WEST
                else -> if (velocity.y > 0) Direction.NORTH else Direction.SOUTH
            }
        }
        if (isMoving) {
            if (!animation.isPlaying || animation.frames.contentEquals(intArrayOf(0))) {
                animation.frames = intArrayOf(1, 0, 2)
                animation.isPlaying = true
                animation.currentFrame = 0
                animation.elapsedTime = 0f
            }
        } else {
            if (animation.isPlaying || !animation.frames.contentEquals(intArrayOf(0))) {
                animation.frames = intArrayOf(0)
                animation.isPlaying = false
                animation.currentFrame = 0
                animation.elapsedTime = 0f
            }
        }
        if (animation.isPlaying) {
            animation.elapsedTime += deltaTime
            if (animation.elapsedTime >= animation.frameDuration) {
                animation.elapsedTime -= animation.frameDuration
                animation.currentFrame = (animation.currentFrame + 1) % animation.frames.size
            }
        }
    }
}
