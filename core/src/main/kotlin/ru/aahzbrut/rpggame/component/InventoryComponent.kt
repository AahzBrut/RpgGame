package ru.aahzbrut.rpggame.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import ru.aahzbrut.rpggame.data.ItemType

class InventoryComponent : Component<InventoryComponent> {
    companion object : ComponentType<InventoryComponent>() {
        const val INVENTORY_CAPACITY = 24
    }

    val items = mutableSetOf<Entity>()
    val itemsToAdd = mutableListOf<ItemType>()

    override fun type() = InventoryComponent
}
