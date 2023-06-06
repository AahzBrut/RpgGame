package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.github.quillraven.fleks.collection.compareEntityBy
import ru.aahzbrut.rpggame.component.ImageComponent

class RenderSystem(
    private val stage: Stage = inject(),
) : IteratingSystem(
    family { all(ImageComponent)},
    comparator = compareEntityBy(ImageComponent)
) {

    override fun onTick() {
        super.onTick()

        with(stage){
            viewport.apply()
            act(deltaTime)
            draw()
        }
    }

    override fun onTickEntity(entity: Entity) {
        entity[ImageComponent].image.toFront()
    }
}
