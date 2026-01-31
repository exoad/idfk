package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.ecs.component.*
import net.exoad.idfk.shared.WorldConfig

class CollisionSystem : IteratingSystem(
    allOf(
        PlayerComponent::class,
        PositionComponent::class,
        VelocityComponent::class,
        SizeComponent::class
    ).get()
) {
    private val positionMapper = mapperFor<PositionComponent>()
    private val velocityMapper = mapperFor<VelocityComponent>()
    private val sizeMapper = mapperFor<SizeComponent>()
    private val playerMapper = mapperFor<PlayerComponent>()
    private val platformMapper = mapperFor<PlatformComponent>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity[positionMapper]!!
        val velocity = entity[velocityMapper]!!
        val size = entity[sizeMapper]!!
        val player = entity[playerMapper]!!
        if (!player.grounded) {
            velocity.y -= WorldConfig.GRAVITY * deltaTime
        }
        position.x += velocity.x * deltaTime
        engine.entities.forEach { other ->
            if (other != entity && other[platformMapper] != null) {
                val otherPos = other[positionMapper]!!
                val otherSize = other[sizeMapper]!!
                val platform = other[platformMapper]!!
                if (position.x < otherPos.x + otherSize.width && position.x + size.width > otherPos.x &&
                    position.y < otherPos.y + otherSize.height && position.y + size.height > otherPos.y
                ) {
                    val canCollideSide =
                        if (velocity.x > 0) PlatformComponent.COLLIDE_WEST else PlatformComponent.COLLIDE_EAST
                    if (platform.canCollide(canCollideSide)) {
                        position.x -= velocity.x * deltaTime
                        velocity.x = 0f
                    }
                }
            }
        }
        position.x = position.x.coerceIn(
            WorldConfig.SCREEN_LEFT,
            WorldConfig.SCREEN_RIGHT - size.width
        )
        position.y += velocity.y * deltaTime
        player.grounded = false
        engine.entities.forEach { other ->
            if (other != entity && other[platformMapper] != null) {
                val otherPos = other[positionMapper]!!
                val otherSize = other[sizeMapper]!!
                val platform = other[platformMapper]!!
                if (position.x < otherPos.x + otherSize.width && position.x + size.width > otherPos.x &&
                    position.y < otherPos.y + otherSize.height && position.y + size.height > otherPos.y
                ) {
                    if (velocity.y <= 0 && platform.canCollide(PlatformComponent.COLLIDE_NORTH)) {
                        position.y = otherPos.y + otherSize.height
                        velocity.y = 0f
                        player.grounded = true
                        player.coyoteTimer = 0f
                    } else if (velocity.y > 0 && platform.canCollide(
                            PlatformComponent.COLLIDE_SOUTH
                        )
                    ) {
                        position.y -= velocity.y * deltaTime
                        velocity.y = 0f
                    }
                }
            }
        }
        // Prevent falling below the screen
        if (position.y <= WorldConfig.SCREEN_BOTTOM && velocity.y <= 0) {
            position.y = WorldConfig.SCREEN_BOTTOM
            velocity.y = 0f
            player.grounded = true
            player.coyoteTimer = 0f
        }
        position.y = position.y.coerceIn(
            WorldConfig.SCREEN_BOTTOM,
            WorldConfig.SCREEN_TOP - size.height
        )
    }
}
