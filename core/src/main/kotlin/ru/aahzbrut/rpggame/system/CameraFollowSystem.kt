package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ktx.tiled.height
import ktx.tiled.width
import ru.aahzbrut.rpggame.component.ImageComponent
import ru.aahzbrut.rpggame.component.PlayerComponent
import ru.aahzbrut.rpggame.event_bus.EventBus
import ru.aahzbrut.rpggame.event_bus.event.MapChangedEvent
import kotlin.math.max
import kotlin.math.min

class CameraFollowSystem(
    eventBus: EventBus = inject(),
    gameStage: Stage = inject("gameStage")
) : IteratingSystem(
    family { all(PlayerComponent, ImageComponent) }
) {
    private val camera = gameStage.camera
    private var mapWidth = 0f
    private var mapHeight = 0f

    init {
        eventBus.onEvent(::handleMapChangedEvent)
    }

    @Suppress("kotlin:S6518")
    override fun onTickEntity(entity: Entity) {
        val viewHalfWidth = camera.viewportWidth * 0.5f
        val viewHalfHeight = camera.viewportHeight * 0.5f

        val cameraMinWidth = min(viewHalfWidth,mapWidth - viewHalfWidth)
        val cameraMaxWidth = max(viewHalfWidth, mapWidth - viewHalfWidth)
        val cameraMinHeight = min(viewHalfHeight,mapHeight - viewHalfHeight)
        val cameraMaxHeight = max(viewHalfHeight, mapHeight - viewHalfHeight)

        entity[ImageComponent].let { imageComponent ->
            imageComponent.image.run {
                camera.position.set(
                    x.coerceIn(cameraMinWidth, cameraMaxWidth),
                    y.coerceIn(cameraMinHeight, cameraMaxHeight),
                    camera.position.z
                )
            }
        }
    }

    private fun handleMapChangedEvent(event: MapChangedEvent) {
        mapWidth = event.map.width.toFloat()
        mapHeight = event.map.height.toFloat()
    }
}
