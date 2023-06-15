package ru.aahzbrut.rpggame.ui.model

import com.github.quillraven.fleks.World
import ru.aahzbrut.rpggame.component.InventoryComponent
import ru.aahzbrut.rpggame.component.ItemComponent
import ru.aahzbrut.rpggame.component.PlayerComponent
import ru.aahzbrut.rpggame.event_bus.EventBus
import ru.aahzbrut.rpggame.event_bus.event.GotItemEvent

class InventoryModel(
    private val world: World,
    eventBus: EventBus,
) : PropertyChangeNotifier() {
    var playerItems by propertyNotify(listOf<ItemModel>())

    init {
        eventBus.onEvent(::onGotItemEvent)
    }

    private fun onGotItemEvent(event: GotItemEvent) {
        with(world) {
            if (event.receiver has PlayerComponent) {
                val playerInventory = event.receiver[InventoryComponent]
                playerItems = playerInventory.items.filterNotNull().map {
                    val item = it[ItemComponent]
                    ItemModel(
                        event.item.id,
                        item.itemType.category,
                        item.itemType.atlasKey,
                        item.slotIndex,
                        item.isEquipped
                    )
                }
            }
        }
    }
}
