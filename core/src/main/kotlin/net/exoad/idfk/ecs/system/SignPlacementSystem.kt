package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.Shared
import net.exoad.idfk.Shared.Keybinds.isJustPressed
import net.exoad.idfk.ecs.component.PlayerComponent
import net.exoad.idfk.ecs.component.PositionComponent
import net.exoad.idfk.util.TileCoordinate
import net.exoad.idfk.util.WorldObjectRegistry
import net.exoad.idfk.world.WorldManager

class SignPlacementSystem(
    private val crosshairMovementSystem: CrosshairMovementSystem
) : IteratingSystem(
    allOf(
        PlayerComponent::class,
        PositionComponent::class,
    ).get()
) {
    private val positionMapper = mapperFor<PositionComponent>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val playerPosition = entity[positionMapper] ?: return
        if (Shared.Keybinds.INTERACT.isJustPressed()) {
            val playerTile = TileCoordinate.fromWorldPixels(playerPosition.x, playerPosition.y)
            val crosshairTile = crosshairMovementSystem.getCrosshairTile()
            if (!crosshairTile.isWithinRange(playerTile, Shared.Player.MAX_REACH_RANGE)) {
                return
            }
            val world = WorldManager["base"]
            val objectsAtTile = world.objectGrid.objectsAt(crosshairTile.x, crosshairTile.y)
            val existingSignpost = objectsAtTile.find { it.type == "signPosts" }
            when {
                existingSignpost != null -> world.objectGrid.removeObject(
                    crosshairTile.x,
                    crosshairTile.y,
                    existingSignpost
                )

                objectsAtTile.isEmpty() -> WorldObjectRegistry.instantiate("signPosts")?.let {
                    world.objectGrid.placeObject(crosshairTile.x, crosshairTile.y, it)
                }
            }
        }
    }
}




