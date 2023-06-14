package ru.aahzbrut.rpggame.ui.model

import com.github.quillraven.fleks.World
import ru.aahzbrut.rpggame.component.LifeComponent
import ru.aahzbrut.rpggame.component.PlayerComponent
import ru.aahzbrut.rpggame.event_bus.EventBus
import ru.aahzbrut.rpggame.event_bus.event.EntityDamagedEvent
import ru.aahzbrut.rpggame.event_bus.event.LootFoundEvent

class GameModel(
    private val world: World,
    eventBus: EventBus,
) : PropertyChangeNotifier() {

    init {
        eventBus.onEvent(::onEntityDamagedEvent)
        eventBus.onEvent(::onLootFoundEvent)
    }

    var playerLifeAmount by propertyNotify(0f)
    var enemyLifeAmount by propertyNotify(0f)
    var lootText by propertyNotify("")

    private fun onEntityDamagedEvent(event: EntityDamagedEvent){
        with(world) {
            val lifeComponent = event.entity.getOrNull(LifeComponent)
            lifeComponent?.let {
                if (event.entity has PlayerComponent) {
                    playerLifeAmount = lifeComponent.currentValue / lifeComponent.maxValue
                } else {
                    enemyLifeAmount = lifeComponent.currentValue / lifeComponent.maxValue
                }
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onLootFoundEvent(ignored: LootFoundEvent){
        lootText = "You found some [BLUE]incredible[] stuff!"
    }
}
