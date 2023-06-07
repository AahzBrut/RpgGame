package ru.aahzbrut.rpggame.component

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ktx.math.vec2

class MovementComponent(
    var speed: Float = 0f,
    val direction: Vector2 = vec2()
) : Component<MovementComponent> {
    override fun type() = MovementComponent

    companion object : ComponentType<MovementComponent>()
}
