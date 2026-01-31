package net.exoad.idfk.factories

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import net.exoad.idfk.ecs.component.ColorComponent
import net.exoad.idfk.ecs.component.IdComponent
import net.exoad.idfk.ecs.component.PositionComponent
import net.exoad.idfk.ecs.component.TextComponent
import net.exoad.idfk.ecs.attach

object TextFactory {
    fun createText(
        engine: Engine,
        position: Vector2,
        text: String = "",
        id: String? = null,
        color: Color
    ): Entity {
        val textEntity = Entity().attach {
            +PositionComponent(position.x, position.y)
            +TextComponent(text)
            if (id != null) {
                +IdComponent(id)
            }
            +ColorComponent(color)
        }
        engine.addEntity(textEntity)
        return textEntity
    }
}