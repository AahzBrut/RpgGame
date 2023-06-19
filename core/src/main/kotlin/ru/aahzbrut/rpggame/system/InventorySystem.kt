package ru.aahzbrut.rpggame.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ru.aahzbrut.rpggame.component.DeathComponent
import ru.aahzbrut.rpggame.component.InventoryComponent
import ru.aahzbrut.rpggame.component.ItemComponent
import ru.aahzbrut.rpggame.event_bus.EventBus
import ru.aahzbrut.rpggame.event_bus.event.GotItemEvent

class InventorySystem(
    private val eventBus: EventBus = inject()
) : IteratingSystem(
    family { all(InventoryComponent).none(DeathComponent) }
) {
    override fun onTickEntity(entity: Entity) {
        val inventory = entity[InventoryComponent]

        inventory.itemsToAdd.forEach { itemType ->
            var slotIndex: Int
            world.entity { item ->
                slotIndex = inventory.items.size
                item += ItemComponent(itemType, slotIndex, false)
                inventory.items.add(item)
            }
            eventBus.fire(GotItemEvent(entity))
            if (inventory.items.size == InventoryComponent.INVENTORY_CAPACITY) return@forEach
        }

        inventory.itemsToAdd.clear()
    }
}
