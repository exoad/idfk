package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.ecs.component.*
import net.exoad.idfk.factories.TextFactory
import net.exoad.idfk.utils.v2f
import kotlin.math.abs
import kotlin.math.sin

class HealthOrbSystem : PlayerAwareSystem(
    allOf(
        HealthOrbComponent::class,
        PositionComponent::class,
        SizeComponent::class
    ).get()
) {
    private val orbMapper = mapperFor<HealthOrbComponent>()
    private val orbPositionMapper = mapperFor<PositionComponent>()
    private val orbSizeMapper = mapperFor<SizeComponent>()

    override fun update(deltaTime: Float) {
        val pPos = playerPosition()
        val pSize = playerSize()
        val pHealth = playerHealth()
        if (pPos == null || pSize == null || pHealth == null) {
            super.update(deltaTime)
            return
        }
        val toRemove = mutableListOf<Entity>()
        engine.entities.forEach { e ->
            val orbComp = e[orbMapper]
            val orbPos = e[orbPositionMapper]
            val orbSize = e[orbSizeMapper]
            if (orbComp != null && orbPos != null && orbSize != null) {
                if (orbComp.baseY.isNaN()) {
                    orbComp.baseY = orbPos.y
                }
                orbComp.time += deltaTime
                orbPos.y =
                    orbComp.baseY + sin(orbComp.time * orbComp.bobSpeed) * orbComp.bobAmplitude

                val overlapX =
                    pPos.x < orbPos.x + orbSize.width && pPos.x + pSize.width > orbPos.x
                val overlapY =
                    pPos.y < orbPos.y + orbSize.height && pPos.y + pSize.height > orbPos.y
                if (overlapX && overlapY) {
                    pHealth.health = (pHealth.health + orbComp.amount).coerceIn(
                        0,
                        pHealth.maxHealth
                    )
                    val textEntity = TextFactory.createText(
                        engine,
                        v2f(
                            orbPos.x,
                            orbPos.y + orbSize.height / 2f
                        ),
                        text = "${if (orbComp.amount >= 0) "+" else "-"}${
                            abs(
                                orbComp.amount
                            )
                        }",
                        color = if (orbComp.amount >= 0) Color.GREEN.cpy() else Color.RED.cpy()
                    )
                    textEntity.add(
                        FloatingTextComponent(
                            lifetime = 0.8f,
                            velocityY = 40f,
                            startAlpha = 1f
                        )
                    )
                    val cComp = textEntity[mapperFor<ColorComponent>()]
                    if (cComp != null) {
                        cComp.color.a = 1f
                    }
                    toRemove.add(e)
                }
            }
        }
        toRemove.forEach {
            engine.removeEntity(it)
        }
        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
    }
}
