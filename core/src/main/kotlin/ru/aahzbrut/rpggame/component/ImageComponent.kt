package ru.aahzbrut.rpggame.component

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

class ImageComponent : Component<ImageComponent>, Comparable<ImageComponent> {
    lateinit var image: Image

    override fun type() = ImageComponent

    companion object : ComponentType<ImageComponent>()

    override fun compareTo(other: ImageComponent): Int {
        val yDiff = other.image.imageY.compareTo(image.imageY)
        return if (yDiff != 0) {
            yDiff
        } else {
            other.image.imageX.compareTo(image.imageX)
        }
    }
}
