package net.exoad.idfk.ecs.component

import com.badlogic.ashley.core.Component
import net.exoad.idfk.Bool

data class AnimationComponent(
    var frames: IntArray,
    val frameDuration: Float = 0.1f,
    var looping: Bool = true,
    var isPlaying: Bool = false,
    var currentFrame: Int = 0,
    var elapsedTime: Float = 0f
) : Component {
    override fun equals(other: Any?): Bool {
        return this === other
               || (other is AnimationComponent
                   && frames.contentEquals(other.frames)
                   && frameDuration == other.frameDuration
                   && looping == other.looping
                   && isPlaying == other.isPlaying
                   && currentFrame == other.currentFrame
                   && elapsedTime == other.elapsedTime)
    }

    override fun hashCode(): Int {
        var result = frames.contentHashCode()
        result = 31 * result + frameDuration.hashCode()
        result = 31 * result + looping.hashCode()
        result = 31 * result + isPlaying.hashCode()
        result = 31 * result + currentFrame
        result = 31 * result + elapsedTime.hashCode()
        return result
    }
}
