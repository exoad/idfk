package net.exoad.idfk.levels

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Color
import net.exoad.idfk.ecs.component.PlatformComponent
import net.exoad.idfk.factories.HealthOrbFactory
import net.exoad.idfk.factories.PlatformFactory
import net.exoad.idfk.factories.PlayerFactory
import net.exoad.idfk.factories.TextFactory
import net.exoad.idfk.utils.v2f

class Level0 : Level {
    override fun load(engine: Engine) {
        with(PlayerFactory) {
            createPlayer(engine, v2f(300f, 360f), health = 50, maxHealth = 100)
        }

        with(PlatformFactory) {
            createPlatform(
                engine,
                v2f(300f, 0f),
                arrayOf(PlatformComponent.COLLIDE_ALL)
            )
            createPlatform(
                engine,
                v2f(600f, 180f),
                arrayOf(PlatformComponent.COLLIDE_NORTH)
            )
            createPlatform(
                engine,
                v2f(1200f, 225f),
                arrayOf(PlatformComponent.COLLIDE_ALL)
            )
            createPlatform(
                engine,
                v2f(1800f, 337.5f),
                arrayOf(
                    PlatformComponent.COLLIDE_WEST,
                    PlatformComponent.COLLIDE_EAST
                )
            )
        }

        with(TextFactory) {
            createText(
                engine,
                v2f(1800f, 393.75f),
                "LEFT & RIGHT",
                color = Color.BLACK
            )
            createText(
                engine,
                v2f(1200f, 281.25f),
                "ALL SIDES",
                color = Color.BLACK
            )
            createText(
                engine,
                v2f(600f, 168.75f),
                "TOP ONLY",
                color = Color.BLACK
            )
        }

        HealthOrbFactory.createHealthOrb(
            engine,
            v2f(360f, 450f),
            amount = 25,
            id = "health_orb_1"
        )
    }
}