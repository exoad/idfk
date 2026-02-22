package net.exoad.idfk

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class GameMain : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()
        addScreen(Screen())
        setScreen<Screen>()
    }
}

