package ru.aahzbrut.rpggame.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.quillraven.fleks.world
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.scene2d.actors
import ru.aahzbrut.rpggame.UI_WINDOW_HEIGHT
import ru.aahzbrut.rpggame.UI_WINDOW_WIDTH
import ru.aahzbrut.rpggame.addGdxInputProcessor
import ru.aahzbrut.rpggame.event_bus.EventBus
import ru.aahzbrut.rpggame.ui.inventoryView
import ru.aahzbrut.rpggame.ui.model.InventoryModel
import ru.aahzbrut.rpggame.ui.view.InventoryView

class InventoryScreen : KtxScreen, KtxInputAdapter {
    private val stage: Stage = Stage(ExtendViewport(UI_WINDOW_WIDTH, UI_WINDOW_HEIGHT))
    private val world = world {}
    private val eventBus = EventBus()
    private val model: InventoryModel = InventoryModel(world, eventBus)
    private lateinit var inventoryView: InventoryView

    override fun show() {
        stage.clear()
        stage.actors {
            inventoryView = inventoryView(model)
        }
        addGdxInputProcessor(stage)
    }

    override fun render(delta: Float) {
        when {
            Gdx.input.isKeyJustPressed(Input.Keys.R) -> {
                hide()
                show()
            }

            Gdx.input.isKeyJustPressed(Input.Keys.D) -> {
                stage.isDebugAll = !stage.isDebugAll
            }
        }

        with(stage) {
            viewport.apply()
            act(delta)
            draw()
        }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun dispose() {
        stage.disposeSafely()
        world.dispose()
    }
}
