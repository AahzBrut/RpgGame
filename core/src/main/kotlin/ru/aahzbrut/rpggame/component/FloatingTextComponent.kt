package ru.aahzbrut.rpggame.component

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ktx.math.vec2

class FloatingTextComponent(
    val startLocation: Vector2 = vec2(),
    val targetLocation: Vector2 = vec2(),
    var lifeSpan: Float = 0f,
    var time: Float = 0f,
) : Component<FloatingTextComponent> {
    companion object : ComponentType<FloatingTextComponent>()

    lateinit var label: Label


    override fun type() = FloatingTextComponent
}
