package ru.aahzbrut.rpggame.screen

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.app.gdxError
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ru.aahzbrut.rpggame.WINDOW_HEIGHT
import ru.aahzbrut.rpggame.WINDOW_WIDTH
import ru.aahzbrut.rpggame.component.ImageComponent
import ru.aahzbrut.rpggame.component.PhysicsComponent
import ru.aahzbrut.rpggame.component.PhysicsComponent.Companion.onPhysicAdd
import ru.aahzbrut.rpggame.component.PhysicsComponent.Companion.onPhysicRemove
import ru.aahzbrut.rpggame.input.KeyBindings
import ru.aahzbrut.rpggame.event.MapChangedEvent
import ru.aahzbrut.rpggame.system.*

class GameScreen : KtxScreen {
    companion object {
        val logger = ktx.log.logger<GameScreen>()
    }

    private val stage: Stage = Stage(ExtendViewport(WINDOW_WIDTH, WINDOW_HEIGHT))
    private val charactersAtlas = TextureAtlas("graphics/characters/Characters.atlas")
    private var currentMap: TiledMap? = null
    private val physicsWorld: World = createWorld(Vector2.Zero).apply {
        autoClearForces = false
    }
    private val keyBindings = KeyBindings()

    private val world = world {
        injectables {
            add(stage)
            add(charactersAtlas)
            add(physicsWorld)
            add(keyBindings)
        }

        components {
            onAdd(ImageComponent) { _, imageComponent -> stage.addActor(imageComponent.image) }
            onRemove(ImageComponent) { _, imageComponent -> stage.root.removeActor(imageComponent.image) }
            onAdd(PhysicsComponent, onPhysicAdd)
            onRemove(PhysicsComponent, onPhysicRemove)
        }

        systems {
            add(InputSystem())
            add(EntitySpawnSystem())
            add(MovementSystem())
            add(PhysicsSystem())
            add(AnimationSystem())
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
        physicsWorld.disposeSafely()
    }
}
