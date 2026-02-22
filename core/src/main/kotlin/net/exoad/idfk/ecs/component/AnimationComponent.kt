package net.exoad.idfk.ecs.component

import com.badlogic.ashley.core.Component

data class AnimationComponent(
    var frames: IntArray,
    val frameDuration: Float = 0.1f,
    var looping: Boolean = true,
    var isPlaying: Boolean = false,
    var currentFrame: Int = 0,
    var elapsedTime: Float = 0f
) : Component {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is AnimationComponent
            || !frames.contentEquals(other.frames)
            || frameDuration != other.frameDuration
            || looping != other.looping
            || isPlaying != other.isPlaying
            || currentFrame != other.currentFrame
            || elapsedTime != other.elapsedTime
        ) {
            return false
        }
        return true
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

