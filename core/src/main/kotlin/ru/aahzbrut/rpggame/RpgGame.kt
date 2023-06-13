package ru.aahzbrut.rpggame

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ru.aahzbrut.rpggame.screen.GameScreen

class RpgGame : KtxGame<KtxScreen>() {

    override fun create() {
        addScreen(GameScreen())
        setScreen<GameScreen>()
    }
}
