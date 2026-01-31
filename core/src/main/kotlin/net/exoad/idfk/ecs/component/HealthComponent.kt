package net.exoad.idfk.ecs.component

import com.badlogic.ashley.core.Component

data class HealthComponent(var health: Int, var maxHealth: Int) : Component