package net.exoad.idfk.util

import net.exoad.idfk.Str

object Logger {
    fun info(message: Str) {
        println("[INFO] $message")
    }

    fun error(message: Str) {
        println("[ERROR] $message")
    }

    fun debug(message: Str) {
        println("[DEBUG] $message")
    }

    fun warn(message: Str) {
        println("[WARN] $message")
    }
}
