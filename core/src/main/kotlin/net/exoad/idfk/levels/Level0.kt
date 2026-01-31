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
            createPlayer(engine, v2f(300f, 360f))
        }

        with(PlatformFactory) {
            createPlatform(engine, v2f(300f, 0f), arrayOf(PlatformComponent.COLLIDE_ALL))
            createPlatform(engine, v2f(600f, 180f), arrayOf(PlatformComponent.COLLIDE_TOP))
            TextFactory.createText(engine, v2f(600f, 168.75f), "TOP ONLY", color = Color.BLACK)
            createPlatform(engine, v2f(1200f, 225f), arrayOf(PlatformComponent.COLLIDE_ALL))
            TextFactory.createText(engine, v2f(1200f, 281.25f), "ALL SIDES", color = Color.BLACK)
            createPlatform(
                engine,
                v2f(1800f, 337.5f),
                arrayOf(PlatformComponent.COLLIDE_LEFT, PlatformComponent.COLLIDE_RIGHT)
            )
            TextFactory.createText(engine, v2f(1800f, 393.75f), "LEFT & RIGHT", color = Color.BLACK)
        }

        TextFactory.createText(engine, v2f(30f, 1035f), "Player: (0, 0)", "player_position", color = Color.BLACK)
        TextFactory.createText(engine, v2f(30f, 990f), "Grounded: false", "player_grounded", color = Color.BLACK)
    }
}