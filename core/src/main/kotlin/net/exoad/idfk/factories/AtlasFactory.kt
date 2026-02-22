package net.exoad.idfk.factories

import net.exoad.idfk.Bool
import net.exoad.idfk.Str
import net.exoad.idfk.ecs.component.AnimationComponent
import net.exoad.idfk.ecs.component.AtlasComponent

object AtlasFactory {
    fun createStaticAtlas(
        texturePath: Str,
        frameWidth: Int,
        frameHeight: Int,
        framesPerRow: Int,
        frameIndex: Int
    ): AtlasComponent {
        return AtlasComponent(
            texturePath = texturePath,
            frameWidth = frameWidth,
            frameHeight = frameHeight,
            framesPerRow = framesPerRow,
            frameIndex = frameIndex
        )
    }

    fun createAnimatedAtlas(
        texturePath: Str,
        frameWidth: Int,
        frameHeight: Int,
        framesPerRow: Int
    ): AtlasComponent {
        return AtlasComponent(
            texturePath = texturePath,
            frameWidth = frameWidth,
            frameHeight = frameHeight,
            framesPerRow = framesPerRow,
            frameIndex = null
        )
    }

    fun createIdleAnimation(
        frameIndex: Int = 0,
        frameDuration: Float = 0.15f
    ): AnimationComponent {
        return AnimationComponent(
            frames = intArrayOf(frameIndex),
            frameDuration = frameDuration,
            looping = true,
            isPlaying = false
        )
    }

    fun createAnimation(
        frames: IntArray,
        frameDuration: Float = 0.15f,
        looping: Bool = true,
        isPlaying: Bool = false
    ): AnimationComponent {
        return AnimationComponent(
            frames = frames,
            frameDuration = frameDuration,
            looping = looping,
            isPlaying = isPlaying
        )
    }
}
