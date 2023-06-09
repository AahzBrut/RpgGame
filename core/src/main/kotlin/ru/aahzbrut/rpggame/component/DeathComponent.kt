package ru.aahzbrut.rpggame.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

class DeathComponent(
    var reviveTime: Float = 0f,
) : Component<DeathComponent> {
    override fun type() = DeathComponent

    companion object : ComponentType<DeathComponent>()
}
