package net.exoad.idfk.factories

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import net.exoad.idfk.Vec2
import net.exoad.idfk.ecs.attach
import net.exoad.idfk.ecs.component.PositionComponent
import net.exoad.idfk.world.World

object TileMapFactory {
    fun createFromWorld(engine: Engine, world: World, position: Vec2 = Vec2(0f, 0f)) {
        engine.addEntity(
            Entity().attach {
                +PositionComponent(position.x, position.y)
                +world.tileMapComponent
                +world.tileSetComponent
            }
        )
    }
}
