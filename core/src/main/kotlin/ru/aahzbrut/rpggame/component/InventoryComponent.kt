package ru.aahzbrut.rpggame.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

class InventoryComponent : Component<InventoryComponent> {
    companion object : ComponentType<InventoryComponent>()

    override fun type() = InventoryComponent
}
