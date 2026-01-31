package net.exoad.idfk.ecs.component

import com.badlogic.ashley.core.Component

data class FloatingTextComponent(
    var lifetime: Float = 0.8f,
    var timeAlive: Float = 0f,
    var velocityY: Float = 30f,
    var startAlpha: Float = 1f
) : Component
