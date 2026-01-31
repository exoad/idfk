package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.ecs.component.ColorComponent
import net.exoad.idfk.ecs.component.FloatingTextComponent
import net.exoad.idfk.ecs.component.PositionComponent
import net.exoad.idfk.ecs.component.TextComponent

class FloatingTextSystem :
    IteratingSystem(allOf(TextComponent::class, PositionComponent::class, FloatingTextComponent::class).get()) {
    private val textMapper = mapperFor<TextComponent>()
    private val posMapper = mapperFor<PositionComponent>()
    private val floatMapper = mapperFor<FloatingTextComponent>()
    private val colorMapper = mapperFor<ColorComponent>()

    override fun update(deltaTime: Float) {
        val toRemove = mutableListOf<Entity>()
        engine.entities.forEach { e ->
            val floatComp = e[floatMapper]
            val pos = e[posMapper]
            val text = e[textMapper]
            val colorComp = e[colorMapper]
            if (floatComp != null && pos != null && text != null) {
                floatComp.timeAlive += deltaTime
                // Move up
                pos.y += floatComp.velocityY * deltaTime
                // Fade alpha linearly
                if (colorComp != null) {
                    colorComp.color.a =
                        floatComp.startAlpha * (1f - (floatComp.timeAlive / floatComp.lifetime).coerceIn(
                            0f,
                            1f
                        ))
                }
                if (floatComp.timeAlive >= floatComp.lifetime) {
                    toRemove.add(e)
                }
            }
        }
        toRemove.forEach { engine.removeEntity(it) }
        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        // not used
    }
}
