package ru.aahzbrut.rpggame.ui.model

import com.github.quillraven.fleks.World
import ru.aahzbrut.rpggame.event_bus.EventBus

class InventoryModel(
    private val world: World,
    eventBus: EventBus,
) : PropertyChangeNotifier() {
}
