package ru.aahzbrut.rpggame.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

class LifeComponent(
    var currentValue: Float = 0f,
    var maxValue: Float = 0f,
    var regenerationValue: Float = 1f,
    var damageValue: Float = 1f,
) : Component<LifeComponent> {
    val isDead: Boolean get() = currentValue <= 0

    override fun type() = LifeComponent

    companion object : ComponentType<LifeComponent>()
}
