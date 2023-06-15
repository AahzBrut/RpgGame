package ru.aahzbrut.rpggame.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ru.aahzbrut.rpggame.data.ItemType

class ItemComponent(
    val itemType: ItemType,
    var slotIndex: Int = -1,
    var isEquipped: Boolean = false,
) : Component<ItemComponent> {
    companion object : ComponentType<ItemComponent>()


    override fun type() = ItemComponent
}
