package ru.aahzbrut.rpggame

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ru.aahzbrut.rpggame.screen.GameScreen
import ru.aahzbrut.rpggame.screen.InventoryScreen
import ru.aahzbrut.rpggame.screen.UIScreen
import ru.aahzbrut.rpggame.ui.disposeSkin
import ru.aahzbrut.rpggame.ui.loadSkin

class RpgGame : KtxGame<KtxScreen>() {

    override fun create() {
        loadSkin()
        addScreen(GameScreen())
        addScreen(UIScreen())
        addScreen(InventoryScreen())
        setScreen<InventoryScreen>()
    }

    override fun dispose() {
        super.dispose()
        disposeSkin()
    }
}
