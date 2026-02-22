package net.exoad.idfk.util

import net.exoad.idfk.Str
import net.exoad.idfk.Vec2

object WorldObjectRegistry {
    private var nextId = 1

    data class WorldObjectType(
        val id: Int,
        val type: Str,
        val spriteName: Str,
        val width: Float,
        val height: Float,
        val offsetX: Float,
        val offsetY: Float,
        val blocking: Boolean = true
    )

    private val types = mutableMapOf<Str, WorldObjectType>()

    fun register(
        type: Str,
        spriteName: Str,
        width: Float,
        height: Float,
        offsetX: Float = 0f,
        offsetY: Float = 0f,
        blocking: Boolean = true
    ): WorldObjectType {
        val objType = WorldObjectType(
            id = nextId++,
            type = type,
            spriteName = spriteName,
            width = width,
            height = height,
            offsetX = offsetX,
            offsetY = offsetY,
            blocking = blocking
        )
        types[type] = objType
        return objType
    }

    fun registerConverted(
        type: Str,
        spriteName: Str,
        spriteHeight: Float,
        offsetX: Float = 0f,
        offsetY: Float = 0f,
        width: Float,
        height: Float,
        blocking: Boolean = true
    ): WorldObjectType {
        val convertedOffset = CollisionRectConverter.convertFromTopLeft(
            Vec2(offsetX, offsetY),
            Vec2(width, height),
            spriteHeight
        )
        val objType = WorldObjectType(
            id = nextId++,
            type = type,
            spriteName = spriteName,
            width = width,
            height = height,
            offsetX = convertedOffset.offsetX,
            offsetY = convertedOffset.offsetY,
            blocking = blocking
        )
        types[type] = objType
        return objType
    }

    fun get(type: Str): WorldObjectType? {
        return types[type]
    }

    fun instantiate(type: Str): WorldObject? {
        val objType = types[type] ?: return null
        return with(objType) {
            WorldObject(
                id = id,
                type = type,
                width = width,
                height = height,
                offsetX = offsetX,
                offsetY = offsetY,
                blocking = blocking
            )
        }
    }

    fun getAll(): Collection<WorldObjectType> {
        return types.values
    }

    fun clear() {
        types.clear()
        nextId = 1
    }
}

