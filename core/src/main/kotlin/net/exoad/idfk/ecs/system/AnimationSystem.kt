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

class AnimationSystem : IteratingSystem(
    allOf(
        AnimationComponent::class,
        VelocityComponent::class,
        DirectionComponent::class
    ).get()
) {
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
        with(animation) {
            if (isMoving) {
                if (!isPlaying || frames.contentEquals(intArrayOf(0))) {
                    frames = intArrayOf(1, 0, 2)
                    isPlaying = true
                    currentFrame = 0
                    elapsedTime = 0f
                }
            } else {
                if (isPlaying || !frames.contentEquals(intArrayOf(0))) {
                    frames = intArrayOf(0)
                    isPlaying = false
                    currentFrame = 0
                    elapsedTime = 0f
                }
            }
            if (isPlaying) {
                elapsedTime += deltaTime
                if (elapsedTime >= frameDuration) {
                    elapsedTime -= frameDuration
                    currentFrame = (currentFrame + 1) % frames.size
                }
            }
        }
    }
}
