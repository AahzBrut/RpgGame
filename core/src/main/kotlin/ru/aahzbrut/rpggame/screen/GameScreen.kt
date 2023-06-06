package ru.aahzbrut.rpggame.screen

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.app.gdxError
import ktx.assets.disposeSafely
import ru.aahzbrut.rpggame.component.AnimationComponent
import ru.aahzbrut.rpggame.component.ImageComponent
import ru.aahzbrut.rpggame.data.AnimationModel
import ru.aahzbrut.rpggame.data.AnimationType
import ru.aahzbrut.rpggame.data.FacingType
import ru.aahzbrut.rpggame.event.MapChangedEvent
import ru.aahzbrut.rpggame.system.AnimationSystem
import ru.aahzbrut.rpggame.system.RenderSystem

class GameScreen : KtxScreen {
    companion object {
        val logger = ktx.log.logger<GameScreen>()
    }

    private val stage: Stage = Stage(ExtendViewport(16f, 9f))
    private val charactersAtlas = TextureAtlas("assets/graphics/characters/Characters.atlas")
    private var currentMap: TiledMap? = null

    private val world = world {
        injectables {
            add(stage)
            add(charactersAtlas)
        }

        components {
            onAdd(ImageComponent) { _, imageComponent -> stage.addActor(imageComponent.image) }
            onRemove(ImageComponent) { _, imageComponent -> stage.root.removeActor(imageComponent.image) }
        }

        systems {
            add(AnimationSystem())
            add(RenderSystem())
        }
    }

    override fun show() {
        logger.debug { "GameScreen displayed." }

        registerEventListeners()

        loadMap()

        world.entity {
            it += ImageComponent().apply {
                image = Image().apply {
                    setSize(4f, 4f)
                }
            }
            it += AnimationComponent().apply {
                setNextAnimation(AnimationModel.PLAYER, AnimationType.IDLE, FacingType.SOUTH)
            }
        }

        world.entity {
            it += ImageComponent().apply {
                image = Image().apply {
                    setSize(2f, 2f)
                    setPosition(4f, 0f)
                }
            }
            it += AnimationComponent().apply {
                setNextAnimation(AnimationModel.SLIME, AnimationType.IDLE, FacingType.NONE)
            }
        }
    }

    private fun loadMap() {
        currentMap = TmxMapLoader().load("assets/graphics/map/map.tmx")
        currentMap?.let {
            stage.root.fire(MapChangedEvent(it))
        } ?: gdxError("Failed to load TilEd map.")
    }

    private fun registerEventListeners() {
        world.systems.filter { it is EventListener }.forEach {
            stage.addListener(it as EventListener)
        }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        world.update(delta)
    }

    override fun dispose() {
        stage.disposeSafely()
        charactersAtlas.disposeSafely()
        currentMap?.disposeSafely()
        world.dispose()
    }
}
