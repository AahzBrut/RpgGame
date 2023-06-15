package ru.aahzbrut.rpggame.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

class ItemComponent : Component<ItemComponent> {
    companion object : ComponentType<ItemComponent>()

    override fun type() = ItemComponent
}
