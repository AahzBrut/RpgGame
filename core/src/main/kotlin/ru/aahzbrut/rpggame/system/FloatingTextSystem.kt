package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ru.aahzbrut.rpggame.component.FloatingTextComponent

class FloatingTextSystem(
    private val uiStage: Stage = inject("uiStage"),
) : IteratingSystem(
    family { all(FloatingTextComponent) }
) {

    override fun onTickEntity(entity: Entity) {
        with(entity[FloatingTextComponent]) {
            if (time >= lifeSpan) {
                entity.remove()
                return
            }

            time += deltaTime

            val labelLocation = startLocation.cpy()
                .interpolate(targetLocation, (time / lifeSpan).coerceAtMost(1f), Interpolation.smooth2)
            label.setPosition(labelLocation.x, uiStage.viewport.worldHeight - labelLocation.y)
        }
    }
}
