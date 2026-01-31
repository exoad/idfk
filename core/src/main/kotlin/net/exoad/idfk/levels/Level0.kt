package net.exoad.idfk.levels

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Color
import net.exoad.idfk.ecs.component.PlatformComponent
import net.exoad.idfk.factories.PlatformFactory
import net.exoad.idfk.factories.PlayerFactory
import net.exoad.idfk.factories.TextFactory
import net.exoad.idfk.utils.v2f

class Level0 : Level {
    override fun load(engine: Engine) {
        with(PlayerFactory) {
            createPlayer(engine, v2f(100f, 160f))
        }

        with(PlatformFactory) {
            createPlatform(engine, v2f(100f, 0f), arrayOf(PlatformComponent.COLLIDE_ALL))
            createPlatform(engine, v2f(200f, 80f), arrayOf(PlatformComponent.COLLIDE_TOP))
            TextFactory.createText(engine, v2f(200f, 75f), "TOP ONLY", color = Color.BLACK)
            createPlatform(engine, v2f(400f, 100f), arrayOf(PlatformComponent.COLLIDE_ALL))
            TextFactory.createText(engine, v2f(400f, 125f), "ALL SIDES", color = Color.BLACK)
            createPlatform(
                engine,
                v2f(600f, 150f),
                arrayOf(PlatformComponent.COLLIDE_LEFT, PlatformComponent.COLLIDE_RIGHT)
            )
            TextFactory.createText(engine, v2f(600f, 175f), "LEFT & RIGHT", color = Color.BLACK)
        }

        TextFactory.createText(engine, v2f(10f, 460f), "Player: (0, 0)", "player_position", color = Color.BLACK)
        TextFactory.createText(engine, v2f(10f, 440f), "Grounded: false", "player_grounded", color = Color.BLACK)
    }
}