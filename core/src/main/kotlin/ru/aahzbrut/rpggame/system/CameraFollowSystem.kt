package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ktx.tiled.height
import ktx.tiled.width
import ru.aahzbrut.rpggame.component.ImageComponent
import ru.aahzbrut.rpggame.component.PlayerComponent
import ru.aahzbrut.rpggame.event.MapChangedEvent

class CameraFollowSystem(
    gameStage: Stage = inject("gameStage")
) : EventListener, IteratingSystem(
    family { all(PlayerComponent, ImageComponent) }
) {
    private val camera = gameStage.camera
    private var mapWidth = 0f
    private var mapHeight = 0f

    @Suppress("kotlin:S6518")
    override fun onTickEntity(entity: Entity) {
        val viewWidth = camera.viewportWidth * 0.5f
        val viewHeight = camera.viewportHeight * 0.5f

        entity[ImageComponent].let { imageComponent ->
            imageComponent.image.run {
                camera.position.set(
                    x.coerceIn(viewWidth, mapWidth - viewWidth),
                    y.coerceIn(viewHeight, mapHeight - viewHeight),
                    camera.position.z
                )
            }
        }
    }

    override fun handle(event: Event): Boolean {
        if (event is MapChangedEvent) {
            mapWidth = event.map.width.toFloat()
            mapHeight = event.map.height.toFloat()
            return true
        }
        return false
    }
}
