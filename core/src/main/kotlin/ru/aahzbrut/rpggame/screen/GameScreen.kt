package ru.aahzbrut.rpggame.screen

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.quillraven.fleks.world
import ktx.actors.plusAssign
import ktx.app.KtxScreen
import ktx.app.gdxError
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ru.aahzbrut.rpggame.UI_WINDOW_HEIGHT
import ru.aahzbrut.rpggame.UI_WINDOW_WIDTH
import ru.aahzbrut.rpggame.WINDOW_HEIGHT
import ru.aahzbrut.rpggame.WINDOW_WIDTH
import ru.aahzbrut.rpggame.component.FloatingTextComponent
import ru.aahzbrut.rpggame.component.ImageComponent
import ru.aahzbrut.rpggame.component.PhysicsComponent
import ru.aahzbrut.rpggame.component.PhysicsComponent.Companion.onPhysicAdd
import ru.aahzbrut.rpggame.component.PhysicsComponent.Companion.onPhysicRemove
import ru.aahzbrut.rpggame.data.UIStyles
import ru.aahzbrut.rpggame.input.KeyBindings
import ru.aahzbrut.rpggame.event.MapChangedEvent
import ru.aahzbrut.rpggame.system.*

class GameScreen : KtxScreen {
    companion object {
        val logger = ktx.log.logger<GameScreen>()
    }

    private val gameStage: Stage = Stage(ExtendViewport(WINDOW_WIDTH, WINDOW_HEIGHT))
    private val uiStage: Stage = Stage(ExtendViewport(UI_WINDOW_WIDTH, UI_WINDOW_HEIGHT))
    private val charactersAtlas = TextureAtlas("graphics/characters/Characters.atlas")
    private var currentMap: TiledMap? = null
    private val physicsWorld: World = createWorld(Vector2.Zero).apply {
        autoClearForces = false
    }
    private val keyBindings = KeyBindings()
    private val uiStyles = UIStyles()

    private val world = world {
        injectables {
            add("uiStage", uiStage)
            add("gameStage", gameStage)
            add(uiStyles)
            add(charactersAtlas)
            add(physicsWorld)
            add(keyBindings)
        }

        components {
            onAdd(ImageComponent) { _, component -> gameStage.addActor(component.image) }
            onRemove(ImageComponent) { _, component -> gameStage.root.removeActor(component.image) }
            onAdd(PhysicsComponent, onPhysicAdd)
            onRemove(PhysicsComponent, onPhysicRemove)
            onAdd(FloatingTextComponent) { _, component ->
                uiStage.addActor(component.label)
                component.label += fadeOut(component.lifeSpan, Interpolation.pow3OutInverse)
                @Suppress("kotlin:S6518")
                component.targetLocation.set(
                    component.startLocation.x + MathUtils.random(-1.5f, 1.5f),
                    component.startLocation.y + 1.5f,
                )
                component.startLocation.toUiCoordinates()
                component.targetLocation.toUiCoordinates()
            }
            onRemove(FloatingTextComponent) { _, component -> uiStage.root.removeActor(component.label) }
        }

        systems {
            add(InputSystem())
            add(CollisionSpawnSystem())
            add(TileColliderDespawnSystem())
            add(EntitySpawnSystem())
            add(MovementSystem())
            add(AttackSystem())
            add(LootSystem())
            add(PhysicsSystem())
            add(DeathManagementSystem())
            add(LifeManagementSystem())
            add(AnimationSystem())
            add(CameraFollowSystem())
            add(FloatingTextSystem())
            add(RenderSystem())
            add(DebugSystem())
        }
    }

    override fun show() {
        logger.debug { "GameScreen displayed." }
        registerEventListeners()
        loadMap()
    }

    private fun loadMap() {
        currentMap = TmxMapLoader().load("graphics/map/map.tmx")
        currentMap?.let {
            gameStage.root.fire(MapChangedEvent(it))
        } ?: gdxError("Failed to load TilEd map.")
    }

    private fun registerEventListeners() {
        world.systems.filter { it is EventListener }.forEach {
            gameStage.addListener(it as EventListener)
        }
    }

    override fun resize(width: Int, height: Int) {
        gameStage.viewport.update(width, height, true)
        uiStage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        world.update(delta)
    }

    override fun dispose() {
        gameStage.disposeSafely()
        uiStage.disposeSafely()
        uiStyles.disposeSafely()
        charactersAtlas.disposeSafely()
        currentMap.disposeSafely()
        world.dispose()
        physicsWorld.disposeSafely()
    }

    private fun Vector2.toUiCoordinates() {
        gameStage.viewport.project(this)
        uiStage.viewport.unproject(this)
    }
}
