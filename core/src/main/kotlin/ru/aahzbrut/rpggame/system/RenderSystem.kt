package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.github.quillraven.fleks.collection.compareEntityBy
import ktx.assets.disposeSafely
import ktx.graphics.use
import ktx.tiled.forEachLayer
import ru.aahzbrut.rpggame.UNIT_SCALE
import ru.aahzbrut.rpggame.component.ImageComponent
import ru.aahzbrut.rpggame.event_bus.EventBus
import ru.aahzbrut.rpggame.event_bus.event.MapChangedEvent

class RenderSystem(
    eventBus: EventBus = inject(),
    private val gameStage: Stage = inject("gameStage"),
    private val uiStage: Stage = inject("uiStage")
) : IteratingSystem(
    family { all(ImageComponent) },
    comparator = compareEntityBy(ImageComponent)
) {
    private val backgroundLayers = mutableListOf<TiledMapTileLayer>()
    private val foregroundLayers = mutableListOf<TiledMapTileLayer>()
    private val mapRenderer = OrthogonalTiledMapRenderer(null, UNIT_SCALE, gameStage.batch)

    init {
        eventBus.onEvent(::handleMapChangedEvent)
    }

    override fun onTick() {
        super.onTick()

        with(gameStage) {
            viewport.apply()
            AnimatedTiledMapTile.updateAnimationBaseTime()
            mapRenderer.setView(gameStage.camera as OrthographicCamera)

            renderLayers(backgroundLayers)

            act(deltaTime)
            draw()

            renderLayers(foregroundLayers)
        }

        with(uiStage) {
            viewport.apply()
            act(deltaTime)
            draw()
        }
    }

    private fun renderLayers(layers: List<TiledMapTileLayer>) {
        if (layers.isNotEmpty()) {
            gameStage.batch.use {
                layers.forEach { mapRenderer.renderTileLayer(it) }
            }
        }
    }

    override fun onTickEntity(entity: Entity) {
        entity[ImageComponent].image.toFront()
    }

    override fun onDispose() {
        mapRenderer.disposeSafely()
    }

    private fun handleMapChangedEvent(event: MapChangedEvent) {
        backgroundLayers.clear()
        foregroundLayers.clear()
        event.map.forEachLayer<TiledMapTileLayer> { layer ->
            if (layer.name.startsWith("foreground")) {
                foregroundLayers.add(layer)
            } else {
                backgroundLayers.add(layer)
            }
        }
    }
}
