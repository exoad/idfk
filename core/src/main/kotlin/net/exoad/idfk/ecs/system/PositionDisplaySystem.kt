package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.ecs.component.IdComponent
import net.exoad.idfk.ecs.component.TextComponent

class PositionDisplaySystem :
    PlayerAwareSystem(allOf(TextComponent::class).get()) {
    private val idMapper = mapperFor<IdComponent>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val idComp = entity[idMapper]
        if (idComp != null) {
            when (idComp.id) {
                "player_position" -> {
                    playerPosition()?.let {
                        val updatedText =
                            "Player: (${it.x.toInt()}, ${it.y.toInt()})"
                        entity.remove(TextComponent::class.java)
                        entity.add(TextComponent(updatedText))
                    }
                }

                "player_grounded" -> {
                    val grounded = playerComp()?.grounded ?: false
                    val updatedText = "Grounded: $grounded"
                    entity.remove(TextComponent::class.java)
                    entity.add(TextComponent(updatedText))
                }

                "player_speed" -> {
                    val playerVel = playerVelocity()
                    val updatedText = if (playerVel != null) {
                        "SPEED: ${"%.2f".format(playerVel.x)} , ${
                            "%.2f".format(
                                playerVel.y
                            )
                        }"
                    } else {
                        "SPEED: 0.00 , 0.00"
                    }
                    entity.remove(TextComponent::class.java)
                    entity.add(TextComponent(updatedText))
                }

                "player_health" -> {
                    playerHealth()?.let {
                        val updatedText =
                            "Health: ${it.health} / ${it.maxHealth}"
                        entity.remove(TextComponent::class.java)
                        entity.add(TextComponent(updatedText))
                    }
                }
            }
        }
    }
}