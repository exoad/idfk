package net.exoad.idfk.util

data class WorldObject(
    val id: Int,
    val type: String,
    val width: Float = 16f,
    val height: Float = 16f,
    val offsetX: Float = 0f,
    val offsetY: Float = 0f,
    val blocking: Boolean = true,
)
