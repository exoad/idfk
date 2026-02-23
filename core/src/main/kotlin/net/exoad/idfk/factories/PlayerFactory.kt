package net.exoad.idfk.factories

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import net.exoad.idfk.Shared
import net.exoad.idfk.Str
import net.exoad.idfk.Vec2
import net.exoad.idfk.ecs.attach
import net.exoad.idfk.ecs.component.*
import net.exoad.idfk.util.SpriteRegistry

object PlayerFactory {
    fun createPlayer(
        engine: Engine,
        position: Vec2,
        size: Vec2? = null,
        id: Str = "player",
    ): Entity {
        assert(id.isNotBlank())
        val playerSize = size ?: Vec2(Shared.World.TILE_SIZE.toFloat(), Shared.World.TILE_SIZE.toFloat())
        val player = Entity().attach {
            +IdComponent(id)
            +PositionComponent(position.x, position.y)
            +VelocityComponent(0f, 0f)
            +SizeComponent(playerSize.x, playerSize.y)
            +with(SpriteRegistry.getCollisionRect(id)) {
                CollisionComponent(
                    width,
                    height,
                    offsetX,
                    offsetY,
                    blocking = true
                )
            }
            +MovementIntentComponent(0f, 0f)
            +DirectionComponent()
            +AtlasComponent(SpriteRegistry.getSheetPath("player"), Shared.World.TILE_SIZE, Shared.World.TILE_SIZE)
            +AnimationComponent(intArrayOf(0), 0.15f, looping = true, isPlaying = false)
            +PlayerComponent()
        }
        engine.addEntity(player)
        return player
    }
}
