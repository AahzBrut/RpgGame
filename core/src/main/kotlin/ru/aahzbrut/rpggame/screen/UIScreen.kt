package ru.aahzbrut.rpggame.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.scene2d.actors
import ru.aahzbrut.rpggame.UI_WINDOW_HEIGHT
import ru.aahzbrut.rpggame.UI_WINDOW_WIDTH
import ru.aahzbrut.rpggame.component.LifeComponent
import ru.aahzbrut.rpggame.component.PlayerComponent
import ru.aahzbrut.rpggame.event_bus.event.EntityDamagedEvent
import ru.aahzbrut.rpggame.event_bus.EventBus
import ru.aahzbrut.rpggame.ui.disposeSkin
import ru.aahzbrut.rpggame.ui.hudView
import ru.aahzbrut.rpggame.ui.loadSkin
import ru.aahzbrut.rpggame.ui.model.GameModel
import ru.aahzbrut.rpggame.ui.view.HUDView

class UIScreen : KtxScreen {
    private val stage: Stage = Stage(ExtendViewport(UI_WINDOW_WIDTH, UI_WINDOW_HEIGHT))
    private val world = world {}
    private val eventBus = EventBus()
    private val gameModel: GameModel = GameModel(world, eventBus)
    private lateinit var hudView: HUDView
    private val playerEntity: Entity = world.entity {
        it += PlayerComponent()
        it += LifeComponent(15f, 15f)
    }

    init {
        loadSkin()
    }

    override fun show() {
        stage.clear()
        stage.actors {
            hudView = hudView(gameModel)
        }
    }

    override fun render(delta: Float) {
        when {
            Gdx.input.isKeyJustPressed(Input.Keys.R) -> {
                hide()
                show()
            }

            Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) -> {
                with(world) {
                    playerEntity[LifeComponent].currentValue -= 1f
                }
                eventBus.fire(EntityDamagedEvent(playerEntity))
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
        disposeSkin()
    }
}
