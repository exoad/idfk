package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.ecs.component.*
import net.exoad.idfk.shared.KeyBindings

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

    companion object {
        const val SCREEN_LEFT = 0f
        const val SCREEN_RIGHT = 854f
        const val SCREEN_TOP = 480f
        const val SCREEN_BOTTOM = 0f
        const val GRAVITY = 500f
        const val SPEED = 200f
        const val JUMP_SPEED = 300f
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val position = entity[positionMapper]!!
        val velocity = entity[velocityMapper]!!
        val size = entity[sizeMapper]!!
        val player = entity[playerMapper]!!
        velocity.x = 0f
        if (Gdx.input.isKeyPressed(KeyBindings.LEFT)) {
            velocity.x = -SPEED
        } else if (Gdx.input.isKeyPressed(KeyBindings.RIGHT)) {
            velocity.x = SPEED
        }
        if (Gdx.input.isKeyJustPressed(KeyBindings.JUMP) && player.grounded) {
            velocity.y = JUMP_SPEED
            player.grounded = false
        }
        if (!player.grounded) {
            velocity.y -= GRAVITY * deltaTime
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
                    val canCollideSide = if (velocity.x > 0) PlatformComponent.COLLIDE_LEFT else PlatformComponent.COLLIDE_RIGHT
                    if (platform.canCollide(canCollideSide)) {
                        position.x -= velocity.x * deltaTime
                        velocity.x = 0f
                    }
                }
            }
        }
        position.x = position.x.coerceIn(SCREEN_LEFT, SCREEN_RIGHT - size.width)
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
                    if (velocity.y < 0 && platform.canCollide(PlatformComponent.COLLIDE_TOP)) {
                        position.y = otherPos.y + otherSize.height
                        velocity.y = 0f
                        player.grounded = true
                    } else if (velocity.y > 0 && platform.canCollide(PlatformComponent.COLLIDE_BOTTOM)) {
                        position.y -= velocity.y * deltaTime
                        velocity.y = 0f
                    }
                }
            }
        }
        // Prevent falling below the screen
        if (position.y <= SCREEN_BOTTOM && velocity.y < 0) {
            position.y = SCREEN_BOTTOM
            velocity.y = 0f
            player.grounded = true
        }
        position.y = position.y.coerceIn(SCREEN_BOTTOM, SCREEN_TOP - size.height)
    }
}
