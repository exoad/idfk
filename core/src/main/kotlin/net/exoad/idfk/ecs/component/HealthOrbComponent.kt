package net.exoad.idfk.ecs.component

import com.badlogic.ashley.core.Component

data class HealthOrbComponent(
    var amount: Int,
    var bobAmplitude: Float = 4f,
    var bobSpeed: Float = 2f,
    var baseY: Float = Float.NaN,
    var time: Float = 0f
) : Component
