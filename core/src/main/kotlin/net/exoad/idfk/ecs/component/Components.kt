package net.exoad.idfk.ecs.component

import com.badlogic.ashley.core.Component
import net.exoad.idfk.Str
import net.exoad.idfk.world.Direction

class PlayerComponent : Component

data class TileSetComponent(val tilesetPath: Str, val tileSize: Int, val tilesPerRow: Int) : Component

data class AtlasComponent(
    val texturePath: Str,
    val frameWidth: Int,
    val frameHeight: Int,
    val framesPerRow: Int,
    val frameIndex: Int? = null
) : Component

data class PositionComponent(var x: Float = 0f, var y: Float = 0f) : Component

data class SizeComponent(var width: Float, var height: Float) : Component

data class VelocityComponent(var x: Float = 0f, var y: Float = 0f) : Component

@JvmInline
value class TextureComponent(val texturePath: Str) : Component

@JvmInline
value class IdComponent(val id: Str) : Component

data class DirectionComponent(var direction: Direction = Direction.SOUTH) : Component

