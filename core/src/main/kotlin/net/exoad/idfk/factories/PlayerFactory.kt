package net.exoad.idfk.factories

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
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
        val playerSize = size ?: Vec2(16f, 16f)
        val atlasPath = SpriteRegistry.getSheetPath("player") ?: "player.png"
        val player = Entity().attach {
            +IdComponent(id)
            +PositionComponent(position.x, position.y)
            +VelocityComponent(0f, 0f)
            +SizeComponent(playerSize.x, playerSize.y)
            +DirectionComponent()
            +AtlasComponent(atlasPath, 16, 16)
            +AnimationComponent(intArrayOf(0), 0.15f, looping = true, isPlaying = false)
            +PlayerComponent()
        }
        engine.addEntity(player)
        return player
    }
}
