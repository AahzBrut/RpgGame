package ru.aahzbrut.rpggame.component

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentHook
import com.github.quillraven.fleks.ComponentType

class ImageComponent : Component<ImageComponent>, Comparable<ImageComponent> {
    companion object : ComponentType<ImageComponent>(){
        val onImageAdd: ComponentHook<ImageComponent> = { _, component ->
            inject<Stage>("gameStage").addActor(component.image)
        }

        val onImageRemove: ComponentHook<ImageComponent> = { _, component ->
            inject<Stage>("gameStage").root.removeActor(component.image)
        }
    }

    lateinit var image: Image

    override fun type() = ImageComponent

    override fun compareTo(other: ImageComponent): Int {
        val yDiff = other.image.y.compareTo(image.y)
        return if (yDiff != 0) {
            yDiff
        } else {
            other.image.x.compareTo(image.x)
        }
    }
}
