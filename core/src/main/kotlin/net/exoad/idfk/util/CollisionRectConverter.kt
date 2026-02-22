package net.exoad.idfk.util

import net.exoad.idfk.Vec2

object CollisionRectConverter {
    /**
     * Converts a collision rectangle from top-left origin to bottom-left origin.
     * (i prefer this method)
     */
    fun convertFromTopLeft(
        offset: Vec2,
        dimension: Vec2,
        spriteHeight: Float
    ): SpriteRegistry.CollisionRect {
        return SpriteRegistry.CollisionRect(
            offsetX = offset.x,
            offsetY = spriteHeight - offset.y - dimension.y,
            width = dimension.x,
            height = dimension.y
        )
    }

    /** reverse inversion */
    fun convertToTopLeft(
        collision: SpriteRegistry.CollisionRect,
        spriteHeight: Float
    ): SpriteRegistry.CollisionRect {
        return SpriteRegistry.CollisionRect(
            offsetX = collision.offsetX,
            offsetY = spriteHeight - collision.offsetY - collision.height,
            width = collision.width,
            height = collision.height
        )
    }
}

