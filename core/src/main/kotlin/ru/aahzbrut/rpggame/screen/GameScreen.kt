package ru.aahzbrut.rpggame.screen

import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.app.gdxError
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.scene2d.actors
import ru.aahzbrut.rpggame.UI_WINDOW_HEIGHT
import ru.aahzbrut.rpggame.UI_WINDOW_WIDTH
import ru.aahzbrut.rpggame.WINDOW_HEIGHT
import ru.aahzbrut.rpggame.WINDOW_WIDTH
import ru.aahzbrut.rpggame.component.*
import ru.aahzbrut.rpggame.component.BehaviourTreeComponent.Companion.onBehaviourTreeAdd
import ru.aahzbrut.rpggame.component.FloatingTextComponent.Companion.onFloatingTextAdd
import ru.aahzbrut.rpggame.component.FloatingTextComponent.Companion.onFloatingTextRemove
import ru.aahzbrut.rpggame.component.ImageComponent.Companion.onImageAdd
import ru.aahzbrut.rpggame.component.ImageComponent.Companion.onImageRemove
import ru.aahzbrut.rpggame.component.PhysicsComponent.Companion.onPhysicAdd
import ru.aahzbrut.rpggame.component.PhysicsComponent.Companion.onPhysicRemove
import ru.aahzbrut.rpggame.component.StateComponent.Companion.onStateAdd
import ru.aahzbrut.rpggame.data.UIStyles
import ru.aahzbrut.rpggame.event_bus.event.MapChangedEvent
import ru.aahzbrut.rpggame.event_bus.EventBus
import ru.aahzbrut.rpggame.input.KeyBindings
import ru.aahzbrut.rpggame.system.*
import ru.aahzbrut.rpggame.ui.hudView
import ru.aahzbrut.rpggame.ui.model.GameModel
import ru.aahzbrut.rpggame.ui.view.HUDView

class GameScreen : KtxScreen {
    private val eventBus = EventBus()
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
            add(eventBus)
        }

        components {
            onAdd(ImageComponent, onImageAdd)
            onRemove(ImageComponent, onImageRemove)
            onAdd(PhysicsComponent, onPhysicAdd)
            onRemove(PhysicsComponent, onPhysicRemove)
            onAdd(FloatingTextComponent, onFloatingTextAdd)
            onRemove(FloatingTextComponent, onFloatingTextRemove)
            onAdd(StateComponent, onStateAdd)
            onAdd(BehaviourTreeComponent, onBehaviourTreeAdd)
        }

        systems {
            add(InputSystem())
            add(StateSystem())
            add(BehaviourTreeSystem())
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
            add(AudioSystem())
        }
    }

    private val gameModel = GameModel(world, eventBus)
    private lateinit var hudView: HUDView

    override fun show() {
        loadMap()
        uiStage.actors {
            hudView = hudView(gameModel)
        }
    }

    private fun loadMap() {
        currentMap = TmxMapLoader().load("graphics/map/map.tmx")
        currentMap?.let {
            eventBus.fire(MapChangedEvent(it))
        } ?: gdxError("Failed to load TilEd map.")
    }

    override fun resize(width: Int, height: Int) {
        gameStage.viewport.update(width, height, true)
        uiStage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        val dt = delta.coerceAtMost(0.25f)
        GdxAI.getTimepiece().update(dt)
        world.update(dt)
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
}
