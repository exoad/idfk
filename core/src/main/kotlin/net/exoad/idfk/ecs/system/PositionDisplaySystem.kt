package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.ecs.component.IdComponent
import net.exoad.idfk.ecs.component.PlayerComponent
import net.exoad.idfk.ecs.component.PositionComponent
import net.exoad.idfk.ecs.component.TextComponent

class PositionDisplaySystem : IteratingSystem(allOf(TextComponent::class).get()) {
    private val positionMapper = mapperFor<PositionComponent>()
    private val idMapper = mapperFor<IdComponent>()
    private var playerPosition: PositionComponent? = null
    private var playerGrounded: Boolean = false
    private var playerEntity: Entity? = null

    override fun update(deltaTime: Float) {
        if (playerEntity == null) {
            engine.entities.forEach { entity ->
                val idComp = entity[mapperFor<IdComponent>()]
                if (idComp?.id == "player") {
                    playerEntity = entity
                    return@forEach
                }
            }
        }
        playerEntity?.let { player ->
            playerPosition = player[positionMapper]
            val playerComp = player[mapperFor<PlayerComponent>()]
            playerGrounded = playerComp?.grounded ?: false
        }
        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val idComp = entity[idMapper]
        if (idComp != null) {
            when (idComp.id) {
                "player_position" -> {
                    playerPosition?.let { playerPos ->
                        val updatedText = "Player: (${playerPos.x.toInt()}, ${playerPos.y.toInt()})"
                        entity.remove(TextComponent::class.java)
                        entity.add(TextComponent(updatedText))
                    }
                }

                "player_grounded" -> {
                    val updatedText = "Grounded: $playerGrounded"
                    entity.remove(TextComponent::class.java)
                    entity.add(TextComponent(updatedText))
                }
            }
        }
    }
}