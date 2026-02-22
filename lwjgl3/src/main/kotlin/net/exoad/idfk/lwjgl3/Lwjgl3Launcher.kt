@file:JvmName("Lwjgl3Launcher")

package net.exoad.idfk.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import net.exoad.idfk.GameMain

fun main() {
    if (StartupHelper.startNewJvmIfRequired()) {
        return
    }
    Lwjgl3Application(GameMain(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("idfk")
        useVsync(true)
        setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1)
        setWindowedMode(1920, 1080)
        setResizable(false)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
