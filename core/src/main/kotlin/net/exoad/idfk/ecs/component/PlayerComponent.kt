package net.exoad.idfk.ecs.component

import com.badlogic.ashley.core.Component

class PlayerComponent : Component {
    var grounded = false
    var coyoteTimer: Float = 0f
}
