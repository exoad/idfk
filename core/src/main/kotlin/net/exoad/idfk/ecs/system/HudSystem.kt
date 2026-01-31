package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.Color
import net.exoad.idfk.factories.TextFactory
import net.exoad.idfk.utils.v2f

class HudSystem : EntitySystem() {
    private var created = false

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        if (!created) {
            TextFactory.createText(
                engine,
                v2f(30f, 1035f),
                id = "player_position",
                color = Color.BLACK
            )
            TextFactory.createText(
                engine,
                v2f(30f, 990f),
                id = "player_grounded",
                color = Color.BLACK
            )
            TextFactory.createText(
                engine,
                v2f(30f, 945f),
                id = "player_speed",
                color = Color.BLACK
            )
            TextFactory.createText(
                engine,
                v2f(30f, 900f),
                id = "player_health",
                color = Color.BLACK
            )
            created = true
        }
    }
}
